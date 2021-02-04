package com.web.brickbuddy.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customers")
@JsonIgnoreProperties(value = { "check" }, allowGetters = false) // include attributes you do not want in queries.
public class Customer implements Serializable {

	@Transient
	private static final long serialVersionUID = -3621211062593132175L;

	@Transient
	private StringChecker check = new StringChecker(true);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cust_id")
	private long customerID;

	private String firstName = "";
	private String lastName = "";
	private String email = "";
	// customerPassword is stored as an encrypted string after encryptPassword is called.
	@JsonIgnore
	private String password = "";
	@Transient
	private String passwordOld = "";
	@Transient
	private String passwordNew1 = "";
	@Transient
	private String passwordNew2 = "";
	private String passwordSalt = "";
	// TODO encrypt to store, only decrypt if customer is authenticated.
	private String blEmail = "";
	private String blPassword = "";
	private long chosenVendor = 0;
	private String location = "";

	@OneToMany(mappedBy = "myCustomer", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<WantedPart> wantList = new ArrayList<WantedPart>();

	@OneToMany(mappedBy = "ourCustomer", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Vendor> vendorList = new ArrayList<Vendor>();

	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	public Customer() {
	}

	public Customer(String firstName, String lastName, String email, String password, String blEmail, String blPassword,
			String location, Set<Role> roles) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setPassword(password);
		this.setBlEmail(blEmail);
		this.setBlPassword(blPassword);
		this.location = location;
		this.roles = roles;
	}

	public long getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public boolean setFirstName(String firstName) {
		if (check.isValidString(firstName)) {
			this.firstName = check.getGoodString();
			return true;
		}
		return false;
	}

	public String getLastName() {
		return this.lastName;
	}

	public boolean setLastName(String lastName) {
		if (check.isValidString(lastName)) {
			this.lastName = check.getGoodString();
			return true;
		}
		return false;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean setEmail(String email) {
		if (check.isEmail(email)) {
			this.email = check.getGoodString();
			return true;
		}
		return false;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean setPassword(String password) {
		if (check.isValidString(password)) {
			this.password = check.getGoodString();
			return true;
		}
		return false;
	}

	public String getPasswordOld() {
		return this.passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public String getPasswordNew1() {
		return this.passwordNew1;
	}

	public void setPasswordNew1(String passwordNew1) {
		this.passwordNew1 = passwordNew1;
	}

	public String getPasswordNew2() {
		return this.passwordNew2;
	}

	public void setPasswordNew2(String passwordNew2) {
		this.passwordNew2 = passwordNew2;
	}

	public String getBlEmail() {
		return this.blEmail;
	}

	public boolean setBlEmail(String blEmail) {
		if (check.isEmail(blEmail)) {
			this.blEmail = check.getGoodString();
			return true;
		}
		return false;
	}

	public String getBlPassword() {
		return this.blPassword;
	}

	public boolean setBlPassword(String blPassword) {
		if (check.isValidString(blPassword)) {
			this.blPassword = check.getGoodString();
			return true;
		}
		return false;
	}

	public long getChosenVendor() {
		return this.chosenVendor;
	}

	public void setChosenVendor(long chosenVendor) {
		this.chosenVendor = chosenVendor;
	}

	public List<WantedPart> getWantList() {
		return this.wantList;
	}

	public void setWantList(List<WantedPart> wantList) {
		this.wantList = wantList;
	}

	public List<Vendor> getVendorList() {
		return this.vendorList;
	}

	public void setVendorList(List<Vendor> vendorList) {
		this.vendorList = vendorList;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getLocation() {
		return this.location;
	}

	public boolean setLocation(String location) {
		if (check.isValidString(location)) {
			this.location = location;
			return true;
		}
		return false;
	}

	/** Method ValidateCustomer checks the current Customer object to ensure all properties have non-blank or null values. It
	 * prints the customer to the console and if invalid, the first reason why it was found to be invalid.
	 * 
	 * @return boolean - true if the customer information is valid, otherwise false. */
	public boolean ValidateCustomer() {
		// TODO remove this following troubleshooting
		// Print this customer information to the console.
		// System.out.println(this.toString());
		if (!check.isValidString(this.firstName)) {
			System.out.println("isValidCustomer - customer has no first name: " + this.firstName);
			return false;
		}
		if (!check.isValidString(this.lastName)) {
			System.out.println("isValidCustomer - customer has no last name: " + this.lastName);
			return false;
		}
		if (!check.isValidString(this.email)) {
			System.out.println("isValidCustomer - customer has no e-mail address information: " + this.email);
			return false;
		}
		if (!check.isValidString(this.password)) {
			System.out.println("isValidCustomer - customer has no password set: " + this.password);
			return false;
		}
		if (!check.isValidString(this.blEmail)) {
			System.out.println("isValidCustomer - customer has no BrickLink e-mail address information: " + this.blEmail);
			return false;
		}
		if (!check.isValidString(this.blPassword)) {
			System.out.println("isValidCustomer - customer has no BrickLink password information: " + this.blPassword);
			return false;
		}
		if (!check.isValidString(this.location)) {
			System.out.println("isValidCustomer - customer has invalid location information: " + this.location);
			return false;
		}
		System.out.println("isValidCustomer - customer is valid.");
		return true;
	}

	/** Method encryptPassword takes the string password, checks to see if it is a valid String. If it is, it encrypts the
	 * password and stores it as the the this.password class attribute.
	 * 
	 * @param password
	 * @return */
	public boolean encryptPassword(String password) {
		if (!check.isValidString(password)) {
			System.out.println("setEncryptedPassword - Not a valid password: " + password);
			return false;
		}
		password = password.trim();
		try {
			String salt = generateNewSalt();
			String encryptedPassword = generateEncryptedString(password, salt);
			this.password = encryptedPassword;
			this.passwordSalt = salt;
			return true;
		} catch (Exception ex) {
			System.out.println("Out of memory during decoding or other unspecified error.\n" + ex);
			ex.printStackTrace();
		}
		return false;
	}

	/** Method isCorrectPassword encodes the inputPassword with the current user's Base64 encrypted salt and compares it to the
	 * current user's encrypted password. If it matches, it returns true. Otherwise false.
	 * 
	 * @param inputPassword
	 * @return */
	public boolean isCorrectPassword(String inputPassword) {
		if (!check.isValidString(inputPassword)) {
			return false;
		}
		inputPassword = inputPassword.trim();
		try {
			String inputHash = generateEncryptedString(inputPassword, this.passwordSalt);
			if (inputHash.equals(this.password)) {
				return true;
			}
		} catch (Exception ex) {
			System.out.println("Out of memory during decoding or other unspecified error.\n" + ex);
			ex.printStackTrace();
		}
		return false;
	}

	/** Method generateNewSalt uses a SecureRandom 8 byte value based on the SHA1PRNG algorithm, converts it to a string returns
	 * the string after it has been encoded with a Base64 encoder.
	 * 
	 * @return
	 * @throws Exception */
	private String generateNewSalt() throws Exception {
		// Generate a new salt value every time a password is set.
		// Don't use Random. SecureRandom complies with FIPS 140-2.
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		// NIST recommends minimum 4 bytes. Safer to use 8 bytes.
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/** Method generateEncryptedString takes a inputString and Base64 encoded salt value and uses it to generate an encrypted
	 * inputString using the PBKDF2 hash algorithm and returns it as a string.
	 * 
	 * @param inputString
	 * @param salt
	 * @return
	 * @throws Exception */
	private String generateEncryptedString(String inputString, String salt) throws Exception {
		// Get a encrypted inputString using PBKDF2 hash algorithm from
		// https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160; // for SHA1
		int iterations = 20000; // NIST specifies 10000, safer with 20000

		byte[] saltAsBytes = Base64.getDecoder().decode(salt);
		KeySpec spec = new PBEKeySpec(inputString.toCharArray(), saltAsBytes, iterations, derivedKeyLength);
		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

		byte[] encodedBytes = f.generateSecret(spec).getEncoded();
		return Base64.getEncoder().encodeToString(encodedBytes);
	}

	private ArrayList<Long> makeIdList() {
		ArrayList<Long> idList = new ArrayList<Long>();
		for (Vendor tempVendor : this.vendorList) {
			if (tempVendor == null)
				idList.add(0L);
			else
				idList.add(tempVendor.getVendorID());
		}
		return idList;
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + this.customerID + ", firstName=" + this.firstName + ", lastName=" + this.lastName
				+ ", email=" + this.email + ", password=" + this.password + ", passwordOld=" + this.passwordOld
				+ ", passwordNew1=" + this.passwordNew1 + ", passwordNew2=" + this.passwordNew2 + ", passwordSalt="
				+ this.passwordSalt + ", blEmail=" + this.blEmail + ", blPassword=" + this.blPassword + ", chosenVendor="
				+ this.chosenVendor + ", location=" + this.location + ", wantList=" + this.wantList + ", vendorList="
				+ this.makeIdList() + ", roles=" + this.roles + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + ((this.blEmail == null) ? 0 : this.blEmail.hashCode());
		result = prime * result + ((this.blPassword == null) ? 0 : this.blPassword.hashCode());
		result = prime * result + (int) (this.chosenVendor ^ (this.chosenVendor >>> 32));
		result = prime * result + (int) (this.customerID ^ (this.customerID >>> 32));
		result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
		result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
		result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
		result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
		result = prime * result + ((this.passwordNew1 == null) ? 0 : this.passwordNew1.hashCode());
		result = prime * result + ((this.passwordNew2 == null) ? 0 : this.passwordNew2.hashCode());
		result = prime * result + ((this.passwordOld == null) ? 0 : this.passwordOld.hashCode());
		result = prime * result + ((this.passwordSalt == null) ? 0 : this.passwordSalt.hashCode());
		result = prime * result + ((this.roles == null) ? 0 : this.roles.hashCode());
		result = prime * result + ((this.vendorList == null) ? 0 : this.vendorList.hashCode());
		result = prime * result + ((this.wantList == null) ? 0 : this.wantList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (this.blEmail == null) {
			if (other.blEmail != null)
				return false;
		} else if (!this.blEmail.equals(other.blEmail))
			return false;
		if (this.blPassword == null) {
			if (other.blPassword != null)
				return false;
		} else if (!this.blPassword.equals(other.blPassword))
			return false;
		if (this.chosenVendor != other.chosenVendor)
			return false;
		if (this.customerID != other.customerID)
			return false;
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		if (this.firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!this.firstName.equals(other.firstName))
			return false;
		if (this.lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!this.lastName.equals(other.lastName))
			return false;
		if (this.location == null) {
			if (other.location != null)
				return false;
		} else if (!this.location.equals(other.location))
			return false;
		if (this.password == null) {
			if (other.password != null)
				return false;
		} else if (!this.password.equals(other.password))
			return false;
		if (this.passwordNew1 == null) {
			if (other.passwordNew1 != null)
				return false;
		} else if (!this.passwordNew1.equals(other.passwordNew1))
			return false;
		if (this.passwordNew2 == null) {
			if (other.passwordNew2 != null)
				return false;
		} else if (!this.passwordNew2.equals(other.passwordNew2))
			return false;
		if (this.passwordOld == null) {
			if (other.passwordOld != null)
				return false;
		} else if (!this.passwordOld.equals(other.passwordOld))
			return false;
		if (this.passwordSalt == null) {
			if (other.passwordSalt != null)
				return false;
		} else if (!this.passwordSalt.equals(other.passwordSalt))
			return false;
		if (this.roles == null) {
			if (other.roles != null)
				return false;
		} else if (!this.roles.equals(other.roles))
			return false;
		if (this.vendorList == null) {
			if (other.vendorList != null)
				return false;
		} else if (!this.vendorList.equals(other.vendorList))
			return false;
		if (this.wantList == null) {
			if (other.wantList != null)
				return false;
		} else if (!this.wantList.equals(other.wantList))
			return false;
		return true;
	}

}
