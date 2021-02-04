package com.web.brickbuddy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.brickbuddy.model.Customer;
import com.web.brickbuddy.model.Role;
import com.web.brickbuddy.model.ShippingLoc;
import com.web.brickbuddy.model.SignIn;
import com.web.brickbuddy.model.StringChecker;
import com.web.brickbuddy.model.Vendor;
import com.web.brickbuddy.model.WantedPart;
import com.web.brickbuddy.repository.CustomerRepository;
import com.web.brickbuddy.repository.ShippingLocRepository;
import com.web.brickbuddy.repository.VendorRepository;
import com.web.brickbuddy.repository.WantedPartRepository;
import com.web.brickbuddy.webUtilities.BrickLinkInfo;

// CrossOrigin: This is only used at Claim. Security issue. We will have Tomcat and jsNode servers creating separate domains.
@CrossOrigin
@RestController
public class MasterControl {
	StringChecker check = new StringChecker();

	@Autowired
	CustomerRepository customers;

	@Autowired
	WantedPartRepository wantedParts;

	@Autowired
	VendorRepository vendors;

	@Autowired
	ShippingLocRepository shipLocs;

	// Customer section
	@RequestMapping(value = "/login", // This string is just a URL name, we can call it anything.
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody // Can only handle one @RequestBody object at a time.
	public ResponseEntity<Customer> loginCustomer(@RequestBody SignIn attempt) {
		if (!attempt.getUserID().isEmpty() && !attempt.getUserPassword().isEmpty()) {
			Optional<Customer> foundCustomer = this.customers.findByEmail(attempt.getUserID());
			if (foundCustomer.isPresent()) {

				if (foundCustomer.get().isCorrectPassword(attempt.getUserPassword())) {
					System.out.println("Found this customer to login:" + foundCustomer);
					return new ResponseEntity<>(foundCustomer.get(), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	@GetMapping(value = "/findCustomerByEmail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Customer> findCustomer(String email) {
		if (email != null) {
			Optional<Customer> foundCustomer = this.customers.findByEmail(email);
			if (foundCustomer.isPresent()) {
				System.out.println("Found " + foundCustomer.get());
				return new ResponseEntity<>(foundCustomer.get(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/registerCustomer", // This string is just a URL name, we can call it anything.
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
															// Not required if we are returning void.
	@ResponseBody
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer currentCustomer) {
		if (check.isEmail(currentCustomer.getEmail())) {
			// Check for duplicate email addresses since this is supposed to be a unique ID value
			Optional<Customer> duplicateEmail = this.customers.findByEmail(currentCustomer.getEmail());
			if (duplicateEmail.isEmpty()) {
				Set<Role> roles = new HashSet<Role>();
				// Make the new customer a USER. Other roles can be assigned elsewhere.
				roles.add(new Role(1L, "USER"));
				currentCustomer.setRoles(roles);
				Optional<ShippingLoc> customerLoc = shipLocs.findByAbrv(currentCustomer.getLocation());
				if (customerLoc.isPresent()) {
					currentCustomer.setLocation(customerLoc.get().getName() + ", " + customerLoc.get().getCountry());
					System.out.println(
							"registerCustomer - location not an accepted state abbreviation: " + currentCustomer.getLocation());
				} else return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
				// Explicitly encrypt the password and clear the oldPassword field where the front end stored the password.
				currentCustomer.encryptPassword(currentCustomer.getPasswordOld());
				currentCustomer.setPasswordOld("");
				System.out.println("Customer signing up is: " + currentCustomer);
				this.customers.save(currentCustomer); // This inserts currentCustomer in the database.
				return new ResponseEntity<>(this.customers.findByEmail(currentCustomer.getEmail()).get(), HttpStatus.OK);
			}
			HttpHeaders header = new HttpHeaders();
			header.add("Email is already used", "Please enter another email or log in to your account.");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
	}

	@PostMapping(value = "/updateCustomer", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	@ResponseBody
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer) {
		// Check for duplicate email addresses since this is supposed to be a unique ID value. check.getGoodString stores a
		// trimmed value
		System.out.println("updatedCustomer = " + updatedCustomer);
		if (check.isEmail(updatedCustomer.getEmail())) {
			Optional<Customer> matchingCustomer = this.customers.findById(updatedCustomer.getCustomerID());
			if (matchingCustomer.isPresent() & customers.findAllByEmail(check.getGoodString()).size() <= 1) {
				boolean[] errorFlags = { false, false, false, false, false, false };
				if (!updatedCustomer.getFirstName().equals(matchingCustomer.get().getFirstName())) {
					if (!matchingCustomer.get().setFirstName(updatedCustomer.getFirstName()))
						errorFlags[0] = false;
				}
				if (!updatedCustomer.getLastName().equals(matchingCustomer.get().getLastName())) {
					if (!matchingCustomer.get().setLastName(updatedCustomer.getLastName()))
						errorFlags[1] = false;
				}
				if (!updatedCustomer.getEmail().equals(matchingCustomer.get().getEmail())) {
					if (matchingCustomer.get().setEmail(updatedCustomer.getEmail()))
						errorFlags[2] = false;
				}
				if (!updatedCustomer.getBlEmail().equals(matchingCustomer.get().getBlEmail())) {
					if (matchingCustomer.get().setBlEmail(updatedCustomer.getBlEmail()))
						errorFlags[3] = false;
				}
				if (!updatedCustomer.getBlPassword().equals(matchingCustomer.get().getBlPassword())) {
					if (matchingCustomer.get().setBlPassword(updatedCustomer.getBlPassword()))
						errorFlags[4] = false;
				}
				if (!updatedCustomer.getLocation().equals(matchingCustomer.get().getLocation())) {
					Optional<ShippingLoc> homeLoc = shipLocs.findByAbrv(updatedCustomer.getLocation());
					if (homeLoc.isPresent()) {
						matchingCustomer.get().setLocation(homeLoc.get().getName() + ", " + homeLoc.get().getCountry());
					} else
						errorFlags[5] = false;
				}
				for (boolean tempError : errorFlags) {
					if (tempError)
						return new ResponseEntity<>(updatedCustomer, HttpStatus.NOT_ACCEPTABLE);
				}
				System.out.println("Updating customer: " + matchingCustomer.get());
				this.customers.save(matchingCustomer.get()); // This updates matchingCustomer in the database.
				return new ResponseEntity<>(matchingCustomer.get(), HttpStatus.OK);
			}
			HttpHeaders header = new HttpHeaders();
			header.add("Email is already used", "Please enter another email or log in to your account.");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
	}

	@PostMapping(value = "/updatePassword", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	@ResponseBody
	public ResponseEntity<Customer> updatePassword(@RequestBody Customer updatedCustomer) {
		System.out.println("Updating password for updatedCustomer = " + updatedCustomer);
		// Check if the fields are valid (non-null or empty) strings and that the new passwords match.
		if (check.isValidString(updatedCustomer.getPasswordOld()) && check.isValidString(updatedCustomer.getPasswordNew1())
				&& check.isValidString(updatedCustomer.getPasswordNew2())) {
			Optional<Customer> matchingCustomer = this.customers.findById(updatedCustomer.getCustomerID());
			// Check if a customer was found and that the two new passwords match.
			if (matchingCustomer.isPresent() && updatedCustomer.getPasswordNew1().equals(updatedCustomer.getPasswordNew2())) {
				// Finally, only allow the record to be updated if the old password is correct for the matchingCustomer
				if (matchingCustomer.get().isCorrectPassword(updatedCustomer.getPasswordOld())) {
					System.out.println("Updating password of customer: " + matchingCustomer.get());
					matchingCustomer.get().encryptPassword(updatedCustomer.getPasswordNew1());
					customers.save(matchingCustomer.get());
					return new ResponseEntity<>(matchingCustomer.get(), HttpStatus.OK);
				}
				System.out.println("The new passwords do not match " + updatedCustomer.getPasswordNew1() + " "
						+ updatedCustomer.getPasswordNew1());
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
	}

	@GetMapping(value = "/getCustomerList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<ArrayList<Customer>> getCustomerList() {
		ArrayList<Customer> customerList = new ArrayList<>(customers.findAll());
		return new ResponseEntity<>(customerList, HttpStatus.OK);
	}

	// Wanted Part methods
	private List<WantedPart> getWantListByEmail(String email) {
		System.out.println("getWantListByEmail - searching for customer with an email of " + email);
		if (check.isEmail(email)) {
			System.out.println("goodstring is " + check.getGoodString());
			Optional<Customer> foundCustomer = this.customers.findByEmail(check.getGoodString());
			if (foundCustomer.isPresent()) {
				System.out.println(
						"Found " + foundCustomer.get().getFirstName() + " who wants " + foundCustomer.get().getWantList());
				return foundCustomer.get().getWantList();
			}
		}
		return null;
	}

	@GetMapping(value = "/getCustomerWantList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<WantedPart>> getCustomerWantList(String email) {
		List<WantedPart> wantList = getWantListByEmail(email);
		if (wantList == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {
			return new ResponseEntity<>(wantList, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/addWantedPart", // This string is just a URL name, we can call it anything.
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // Can only handle one @RequestBody object at a time.
	public ResponseEntity<List<WantedPart>> addWantedPart(@RequestBody WantedPart addPart) {
		System.out.println("addWantedPart - the addPart is: " + addPart);
		BrickLinkInfo blInfo = new BrickLinkInfo();
		addPart = blInfo.findPartInfo(addPart);
		System.out.println("addWantedPart - the updated addPart is: " + addPart);
		if (addPart != null && addPart.getMyCustomer() != null) {
			Optional<Customer> tempCustomer = customers.findById(addPart.getMyCustomer().getCustomerID());
			Optional<WantedPart> currentWantedPart = wantedParts.findById(addPart.getPartID());
			if (tempCustomer.isPresent() && !currentWantedPart.isPresent()) {
				System.out.println(
						"Adding the following part to " + addPart.getMyCustomer().getFirstName() + "'s wantlist: " + addPart);
				wantedParts.save(addPart);
				// Retrieve the customer's new want list from the database.
				List<WantedPart> wantList = getWantListByEmail(tempCustomer.get().getEmail());
				return new ResponseEntity<>(wantList, HttpStatus.OK);
			} else {
				System.out.println("addWantedPart - Associated customer was not found or part with that id already exists.");
				return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
			}

		}
		System.out.println("addWantedPart - search by type, num, and colorNum failed, or customer not assigned." + addPart);
		return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
	}

	@PostMapping(value = "/removeWantedPart", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	@ResponseBody
	public ResponseEntity<List<WantedPart>> removeWantedPart(@RequestBody WantedPart removePart) {
		System.out.println("removeWantedPart - partID to remove is " + removePart);
		if (wantedParts.existsById(removePart.getPartID())) {
			// Fetch the existing part from the database since removePart may not have customer information.
			Optional<WantedPart> oldPart = wantedParts.findById(removePart.getPartID());
			wantedParts.deleteById(removePart.getPartID());
			List<WantedPart> wantList = getWantListByEmail(oldPart.get().getMyCustomer().getEmail());
			return new ResponseEntity<>(wantList, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(value = "/updateWantedPart", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // Can only handle one @RequestBody object at a time.
	public ResponseEntity<List<WantedPart>> updateWantedPart(@RequestBody WantedPart updatePart) {
		System.out.println("updateWantedPart - the updatePart is: " + updatePart);
		BrickLinkInfo blInfo = new BrickLinkInfo();
		updatePart = blInfo.findPartInfo(updatePart);
		System.out.println("---------------updateWantedPart - the updated updatePart is: " + updatePart);
		Optional<WantedPart> currentWantedPart = wantedParts.findById(updatePart.getPartID());
		if (updatePart != null && currentWantedPart.get().getMyCustomer() != null) {
			Optional<Customer> tempCustomer = customers.findById(currentWantedPart.get().getMyCustomer().getCustomerID());
			if (tempCustomer.isPresent()) {
				currentWantedPart.get().setType(updatePart.getType());
				currentWantedPart.get().setNumber(updatePart.getNumber());
				currentWantedPart.get().setName(updatePart.getName());
				currentWantedPart.get().setColorNum(updatePart.getColorNum());
				currentWantedPart.get().setQuantity(updatePart.getQuantity());
				currentWantedPart.get().setImageURL(updatePart.getImageURL());
				currentWantedPart.get().setCond(updatePart.getCond());
				System.out.println("--------------Updating the following part to "
						+ currentWantedPart.get().getMyCustomer().getFirstName() + "'s wantlist: " + currentWantedPart.get());
				wantedParts.save(currentWantedPart.get());
				List<WantedPart> wantList = getWantListByEmail(tempCustomer.get().getEmail());
				return new ResponseEntity<>(wantList, HttpStatus.OK);
			} else {
				System.out.println("updateWantedPart - Associated customer was not found.");
				return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
			}

		}
		System.out.println("updateWantedPart - search by type, num, and colorNum failed, or customer not assigned." + updatePart);
		return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
	}

//	@Transactional
	// Vendor list section
	@PostMapping(value = "/findMatchingVendors", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public ResponseEntity<List<Vendor>> findMatchingVendors(@RequestBody Customer searchCustomer) {
		System.out.println("**********findMatchingVendors - the customer email is: " + searchCustomer.getEmail());
		BrickLinkInfo blInfo = new BrickLinkInfo();
		List<WantedPart> wantedPartList = new ArrayList<WantedPart>();
		Optional<Customer> dbCustomer = this.customers.findById(searchCustomer.getCustomerID());
		if (!dbCustomer.isPresent()) {
			System.out.println("*************findMatchingVendors - Associated customer was not found.");
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		System.out.println("************findMatchingVendors - the found customer is: " + dbCustomer);
		Customer foundCustomer = dbCustomer.get();
		wantedPartList = foundCustomer.getWantList();
		if (wantedPartList.isEmpty()) {
			System.out.println("************findMatchingVendors - the wantlist is empty.");
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		// Clear out all the previous results for this customer.
		vendors.deleteAll(foundCustomer.getVendorList());
		vendors.flush();
		foundCustomer.setVendorList(new ArrayList<Vendor>());
		customers.saveAndFlush(foundCustomer);
		// Find related customer and vendor ShippingLoc to estimate shipping cost.
		// Location is stored for display as state name, country abrv
		String[] customerLocArray = foundCustomer.getLocation().split(",");
		String customerLocString = "";
		if (customerLocArray.length > 1)
			customerLocString = customerLocArray[0].trim();
		Optional<ShippingLoc> customerLoc = shipLocs.findByName(customerLocString);
		if (!customerLoc.isPresent()) {
			System.out.println("*************findMatchingVendors - Associated customer location was not found for "
					+ foundCustomer.getFirstName());
//// ***********Troubleshooting Only
//return null; 
		}
		// Search for vendors offering the parts in this customer's want list
		boolean foundSome = false;
		List<Vendor> matchingVendors = new ArrayList<Vendor>();
		for (WantedPart tempWant : wantedPartList) {
			System.out.println("************findMatchingVendors - searching Bricklink for WantedPart: " + tempWant);
			matchingVendors = blInfo.findMatchingParts(tempWant, matchingVendors);
			if (!matchingVendors.isEmpty()) {
				foundSome = true;
			}
		}
		if (!foundSome) {
			System.out.println("************findMatchingVendors - no matching vendors found: ");
			return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
		}
		// Now filter and sort the resultant vendors
		List<Vendor> vendorsToRemove = new ArrayList<Vendor>();
		for (int index = 0; index < matchingVendors.size(); index++) {
			Vendor tempVendor = matchingVendors.get(index);
			if (tempVendor.getMatchingParts().size() < 2) {
				// If there are not at least two matching parts, mark vendor to remove from this list
				vendorsToRemove.add(tempVendor);
			} else {
				// Search BrickLink for vendor site to pull location information.
				Vendor updatedVendor = blInfo.findVendorLoc(tempVendor);
				if (updatedVendor == null) {
					System.out.println("************findMatchingVendors - BrickLink did not have location information for "
							+ tempVendor.getName());
				} else {
					// Location is stored for display as state name, country abrv
					String[] vendorLocArray = updatedVendor.getLocation().split(",");
					String vendorLocString = "";
					if (vendorLocArray.length > 1)
						vendorLocString = vendorLocArray[0].trim();
					Optional<ShippingLoc> vendorLoc = shipLocs.findByName(vendorLocString);
					// Some vendors use their state abbreviation instead of the state name
					if (!vendorLoc.isPresent()) {
						System.out.println("*************findMatchingVendors - Associated vendor location: "+ vendorLocArray[0].trim() + " was not found");
						vendorLoc = shipLocs.findByAbrv(vendorLocString);
					}
					if (!vendorLoc.isPresent()) {
						System.out.println("*************findMatchingVendors - Associated vendor location was not found for "
								+ updatedVendor.getName());
//// Troubleshooting only
//return null;
					} else {
						updatedVendor.setShipCost(BigDecimal.valueOf(vendorLoc.get().estShipping(customerLoc.get())));
					}
					// Update the estimated total before updating the vendor in the matchingVendors list.
					updatedVendor.getEstTotal();
					matchingVendors.set(index, updatedVendor);
				}
			}
		}
		// Remove all the vendors who do not have more than one matching part
		matchingVendors.removeAll(vendorsToRemove);
		// Sort by estimate total price (ascending)
		Collections.sort(matchingVendors, Comparator.comparing(Vendor::getEstTotal));
		// Sort by how many matches (descending)
		Collections.sort(matchingVendors, new Comparator<Vendor>() {
			@Override
			public int compare(Vendor v1, Vendor v2) {
				if (v1.getMatches() > v2.getMatches())
					return -1;
				if (v1.getMatches() == v2.getMatches())
					return 0;
				else
					return 1;
			}
		});
		// Save the customer with the updated matchingVendor list to the repository and return the list to the front end.
		foundCustomer.setVendorList(matchingVendors);
		customers.save(foundCustomer);
		return new ResponseEntity<>(matchingVendors, HttpStatus.OK);
	}

	@GetMapping(value = "/getMatchingVendorList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Vendor>> getMatchingVendorList(String email) {
		Optional<Customer> foundCustomer = customers.findByEmail(email);
		if (!foundCustomer.isPresent()) {
			System.out.println("getMatchingVendorList - Associated customer was not found.");
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		List<Vendor> matchingVendors = foundCustomer.get().getVendorList();
		// Sort by estimated total price (ascending)
		Collections.sort(matchingVendors, Comparator.comparing(Vendor::getEstTotal));
		// Sort by how many matches (descending)
		Collections.sort(matchingVendors, new Comparator<Vendor>() {
			@Override
			public int compare(Vendor v1, Vendor v2) {
				if (v1.getMatches() > v2.getMatches())
					return -1;
				if (v1.getMatches() == v2.getMatches())
					return 0;
				else
					return 1;
			}
		});
		return new ResponseEntity<>(matchingVendors, HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateVendorAddedCost", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Vendor>> updateVendorAddedCost(@RequestBody Vendor selectedVendor) {
		Optional<Vendor> foundVendor = vendors.findById(selectedVendor.getVendorID());
		if (!foundVendor.isPresent()) {
			System.out.println("updateVendorAddedCost - Associated vendor was not found.");
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		foundVendor.get().setAddedCost(selectedVendor.getAddedCost());
		vendors.save(foundVendor.get());
		List<Vendor> matchingVendors = foundVendor.get().getOurCustomer().getVendorList();
		return new ResponseEntity<>(matchingVendors, HttpStatus.OK);
	}
	
//	@Transactional
	@GetMapping(value = "/clearMatchingVendorList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> clearMatchingVendorList(String email) {
		System.out.println("*******************clearMatchingVendorList - searching for customer with " + email );
		Optional<Customer> foundCustomer = customers.findByEmail(email);
		if (!foundCustomer.isPresent()) {
			System.out.println("clearMatchingVendorList - Associated customer was not found.");
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		List<Vendor> matchingVendors = foundCustomer.get().getVendorList();
//		for (Vendor vendorToDelete:matchingVendors) {
//			vendors.deleteById(vendorToDelete.getVendorID());
//		}
		vendors.deleteAll(matchingVendors);
		vendors.flush();
		foundCustomer.get().setVendorList(new ArrayList<Vendor>());
		customers.saveAndFlush(foundCustomer.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
