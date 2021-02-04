package com.web.brickbuddy.webUtilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.web.brickbuddy.model.StockedPart;
import com.web.brickbuddy.model.StringChecker;
import com.web.brickbuddy.model.Vendor;
import com.web.brickbuddy.model.WantedPart;

public class BrickLinkInfo {

	StringChecker check = new StringChecker(true);

	public WantedPart findPartInfo(WantedPart searchPart) {
		if (!check.isValidString(searchPart.getType()) || !check.isValidString(searchPart.getNumber())) {
			System.out.println("findPartInfo - Cannot find searchPart. It does not have type, or number info available.");
			return null;
		}
		if (searchPart.getType().equalsIgnoreCase("P") && !check.isValidString(searchPart.getColorNum())) {
			System.out.println("findPartInfo - searchPart type is P, but it does not have color info available.");
			return null;
		}
		WantedPart foundPart = new WantedPart();
		String partSearchString = "";
		if (searchPart.getType().equalsIgnoreCase("M"))
			partSearchString = searchPart.getNumber();
		else if (searchPart.getType().equalsIgnoreCase("P"))
			partSearchString = searchPart.getNumber() + "&colorID=" + searchPart.getColorNum();
		System.out.println(partSearchString);
		String url = "";
		String urlPart1 = "https://www.bricklink.com/search.asp?pg=";
		String urlPart2 = "&q=" + partSearchString;
		String urlPart3 = "&excBind=Y&itemType=" + searchPart.getType();
		String urlPart4 = "&sellerLoc=R&shipCountryID=US&moneyTypeID=1&viewFrom=sa&regionID=3&sz=500&searchSort=P";
		url = urlPart1 + 1 + urlPart2 + urlPart3 + urlPart4;
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			// doc = Jsoup.parse(html);
		} catch (Exception ex) {
			System.out.println("IO error connecting to " + url);
			ex.printStackTrace();
			return null;
		}
		// If the page returned contains from "No Item(s) were found. Please try again!" then pagination may be an issue.
		if (!doc.getElementsContainingText("Please try again").isEmpty()) {
			System.out.println("findPartInfo - invalid URL " + url);
			if (searchPart.getType().equalsIgnoreCase("P")) {
				url = "https://www.bricklink.com/search.asp?viewFrom=sa&shipCountryID=US&regionID=3&moneyTypeID=1&itemType=P&colorID="
						+ searchPart.getColorNum() + "&q=" + searchPart.getNumber()
						+ "&sellerLoc=R&excBind=Y&searchSort=P&sz=500";
			}
			if (searchPart.getType().equalsIgnoreCase("M")) {
				url = "https://www.bricklink.com/search.asp?viewFrom=sa&shipCountryID=US&regionID=3&moneyTypeID=1&itemType=M&q="
						+ searchPart.getNumber() + "&sellerLoc=R&excBind=Y&searchSort=P&sz=500";
			}
			// If this page contains from "No Item(s) were found. Please try again!" then the part is not found, return an empty
			// list.
			if (!doc.getElementsContainingText("Please try again").isEmpty()) {
				System.out.println("URL does not link to a part in the BrickLink inventory");
				return null;
			}
		}
		Elements tables = doc.select("table");
		// The search results are table 8, so index 7
		Elements rows = tables.get(7).select("tr");
		// If there is only a header row in the table then there are no search results returned.
		if (rows.size() <= 1)
			return null;
		Element row = rows.get(1);
		// Skip the header row, start at index 1.
		Elements columns = row.children();
		String imageURL = columns.get(0).getElementsByTag("img").get(0).getElementsByAttribute("src").get(0).attr("src");
		// Minifig expecting <b>PARTNAME </b>
		// Part expecting <b>COLORNAME PARTNAME </b>
		String fullDescription = columns.get(2).getElementsByTag("b").get(0).text().trim();
		String partNum = "";
		String colorNum = "";
		String colorName = "";
		// Minifig expecting <img alt="Lot ID: 227199870 Minifig No: PARTNUM Name: PARTNAME"
		// Part expecting <img alt="Lot ID: 230991965 Part No: PARTNUM Name: PARTNAME"
		String imageAlt = columns.get(0).getElementsByTag("img").get(0).getElementsByAttribute("alt").get(0).attr("alt");
		String partName = imageAlt.split("Name:")[1].trim();
		if (searchPart.getType().equalsIgnoreCase("P")) {
			partNum = imageAlt.split("Part No:")[1].split("Name:")[0].trim();
			String[] splitURL = imageURL.split("/");
			colorNum = splitURL[splitURL.length - 2];
			colorName = fullDescription.replace(partName, "").trim();
		}
		if (searchPart.getType().equalsIgnoreCase("M")) {
			partNum = imageAlt.split("Minifig No:")[1].split("Name:")[0].trim();
		}
		// Split by . needs two escape characters in the string since . is a regex wildcard and \. is not a normal
		// string escape sequence.
		// partNum = partNum.split("\\.")[0];
		// Expecting <b>Used</b> or <b>New</b>
		String condition = columns.get(1).getElementsByTag("b").get(0).text();
		// Ensure the part in the list matches the part/minifigure specified in the search.
		System.out.println("Part - |" + partNum + "| equals |" + searchPart.getNumber() + "|");
		// TODO fix reference to a single partSearch parameter
		if ((searchPart.getType().equalsIgnoreCase("M") && partNum.equals(searchPart.getNumber()))
				|| (!searchPart.getType().equalsIgnoreCase("M") && partNum.equals(searchPart.getNumber())
						&& colorNum.equals(searchPart.getColorNum()))) {
			// Set new StockedPart attributes
			// System.out.println("Updating part with " + partNum + partName + fullDescription + colorNum + colorName);
			foundPart.setPartID(searchPart.getPartID());
			foundPart.setType(searchPart.getType());
			foundPart.setCond(searchPart.getCond());
			foundPart.setQuantity(searchPart.getQuantity());
			foundPart.setNumber(partNum);
			foundPart.setName(partName);
			// Higher resolution images may be found here:
			String bigImageURL = "https://img.bricklink.com/ItemImage/";
			if (searchPart.getType().equalsIgnoreCase("M"))
				bigImageURL += "MN/0/";
			if (searchPart.getType().equalsIgnoreCase("P"))
				bigImageURL += "PN/" + colorNum + "/";
			bigImageURL += partNum + ".png";
			foundPart.setImageURL(bigImageURL);
			// foundPart.setImageURL(imageURL);
			foundPart.setColorNum(colorNum);
			if (searchPart.getMyCustomer() != null)
				foundPart.setMyCustomer(searchPart.getMyCustomer());
		} else {
			System.out.println("Could not find: " + searchPart.getNumber() + " " + searchPart.getColorNum());
			System.out.println("Page url is " + url);
			return null;
		}
		System.out.println("The updated part information is " + foundPart);
		return foundPart;
	}

	public List<Vendor> findMatchingParts(WantedPart searchPart, List<Vendor> vendorList) {
		System.out.println("findMatchingParts - searching for this ID" + searchPart);
		String partSearchString = "";
		if (searchPart.getType().equalsIgnoreCase("M"))
			partSearchString = searchPart.getNumber();
		else
			partSearchString = searchPart.getNumber() + "&colorID=" + searchPart.getColorNum();
		if (searchPart.getCond().equalsIgnoreCase("New")) {
			partSearchString += "&invNew=N";
		} else if (searchPart.getCond().equalsIgnoreCase("Used")) {
			partSearchString += "&invNew=U";
		}
		System.out.println(partSearchString);
		String url = "";
		String urlPart1 = "https://www.bricklink.com/search.asp?pg=";
		String urlPart2 = "&q=" + partSearchString;
		String urlPart3 = "&excBind=Y&itemType=" + searchPart.getType();
		String urlPart4 = "&sellerLoc=R&shipCountryID=US&moneyTypeID=1&viewFrom=sa&regionID=3&sz=500&searchSort=P";
		int lastPage = 1;
		int resultsFound = 0;
		outerLoop: for (int page = 1; page <= lastPage; page++) {
			url = urlPart1 + page + urlPart2 + urlPart3 + urlPart4;
			Document doc = null;
			try {
				doc = Jsoup.connect(url).get();
				// doc = Jsoup.parse(html);
			} catch (Exception ex) {
				System.out.println("IO error connecting to " + url);
				ex.printStackTrace();
				return new ArrayList<Vendor>();
			}
			// If the page returned contains from "No Item(s) were found. Please try again!" then pagination may be an issue.
			if (!doc.getElementsContainingText("Please try again").isEmpty()) {
				System.out.println("findMatchingParts - bad page url " + url);
				if (searchPart.getType().equalsIgnoreCase("P")) {
					url = "https://www.bricklink.com/search.asp?viewFrom=sa&shipCountryID=US&regionID=3&moneyTypeID=1&itemType=P&colorID="
							+ searchPart.getColorNum() + "&q=" + searchPart.getNumber()
							+ "&sellerLoc=R&excBind=Y&searchSort=P&sz=500";
				}
				if (searchPart.getType().equalsIgnoreCase("M")) {
					url = "https://www.bricklink.com/search.asp?viewFrom=sa&shipCountryID=US&regionID=3&moneyTypeID=1&itemType=M&q="
							+ searchPart.getNumber() + "&sellerLoc=R&excBind=Y&searchSort=P&sz=500";
				}
				// If this page contains from "No Item(s) were found. Please try again!" then the part is not found, return an
				// empty list.
				if (!doc.getElementsContainingText("Please try again").isEmpty()) {
					System.out.println("URL does not link to a part in the BrickLink inventory");
					return new ArrayList<Vendor>();
				}
			}
			Elements tables = doc.select("table");
			Elements lotsFound = tables.get(5).getElementsByTag("font").get(0).getElementsByTag("b");
			String totalPages = lotsFound.get(2).text();
			lastPage = Integer.parseInt(totalPages);
			// The search results are table 8, so index 7
			Elements rows = tables.get(7).select("tr");
			System.out.println(rows.size());
			boolean foundFirst = false;
			// Skip the header row, start at index 1.
			for (int index = 1; index < rows.size() - 2; index++) {
				Element row = rows.get(index);
				System.out.println("********Row " + index + "**********");
				Elements columns = row.children();
				String imageURL = columns.get(0).getElementsByTag("img").get(0).getElementsByAttribute("src").get(0).attr("src");
				// Minifig expecting <b>PARTNAME </b>
				// Part expecting <b>COLORNAME PARTNAME </b>
				String partName = columns.get(2).getElementsByTag("b").get(0).text().trim();
				String colorNum = "";
				//
				Elements breadCrumbLinks = columns.get(2).getElementsByTag("font").get(0).getElementsByTag("a");
				String partNum = breadCrumbLinks.get(breadCrumbLinks.size() - 1).text().trim();
				if (searchPart.getType().equalsIgnoreCase("P")) {
					String catalogURI = breadCrumbLinks.get(breadCrumbLinks.size() - 1).getElementsByAttribute("href").get(0)
							.attr("href");
					if (catalogURI.split("colorID=").length > 1) {
						colorNum = catalogURI.split("colorID=")[1].trim();
					} else {
						colorNum = "";
					}
				}
				// Expecting <td><b>PARTNAME </b><br>CONDITIONDESCRIPTION
				String conditionDescription = columns.get(2).text().split("Items For Sale:")[0].trim();
				conditionDescription = conditionDescription.replace(partName, "").trim();
				// Split by . needs two escape characters in the string since . is a regex wildcard and \. is not a normal
				// string escape sequence.
				// partNum = partNum.split("\\.")[0];
				// Expecting <b>Used</b> or <b>New</b>
				String condition = columns.get(1).getElementsByTag("b").get(0).text();
				// Expecting <font color="#606060">Loc: STORECOUNTRY, Min Buy: MINBUYSTRING</font>
				String storeCountryMinBuy = columns.get(2).getElementsByTag("font").get(1).text();
				String storeCountry = storeCountryMinBuy.split("Loc: ")[1].split(", Min")[0];
				String minBuyString = storeCountryMinBuy.split("Min Buy: ")[1];
				int quantity = 0;
				// Expecting <br>Qty:&nbsp;<b>STOCKED QUANTITY</b><br>
				String quantityString = columns.get(3).getElementsByTag("b").get(0).text();
				if (check.isInt(quantityString))
					quantity = check.getGoodInt();
				BigDecimal minBuy = BigDecimal.ZERO;
				BigDecimal cost = BigDecimal.ZERO;
				// Expecting MinBuyString Min Buy: None or Min Buy: ~US $MINBUY
				if (!minBuyString.contains("None") && minBuyString.contains("US $")) {
					if (check.isBigDecimal(minBuyString.substring(5))) {
						minBuy = check.getGoodBigDecimal();
					} else {
						System.out.println("Row " + index + "- Cannot convert min buy string to BigDecimal value remains ZERO");
					}
				}
				// Expecting <b>US $COST</b>
				String costString = columns.get(3).getElementsByTag("font").get(1).text();
				if (check.isBigDecimal(costString.substring(5, costString.length() - 1))) {
					cost = check.getGoodBigDecimal();
				} else {
					System.out.println("Row " + index + "- Cannot convert cost string to BigDecimal value remains ZERO");
				}
				// Expecting relative URI <a href="/store.asp?p=STOREID&amp;itemID=227199870">
				String buyURL = "https://store.bricklink.com" + columns.get(3).select("a[href]").get(0).attr("href");
				String storeURL = buyURL.split("&")[0];
				// Expecting <a href="/store.asp?p=SnarkBricks&amp;itemID=227199870">STORENAME</a>
				String storeName = columns.get(3).select("a[href]").get(0).text().trim();
				// Ensure the part in the list matches the part/minifigure specified in the search.
				System.out.println("Part - |" + partNum + "| equals |" + searchPart.getNumber() + "| want "
						+ searchPart.getQuantity() + " with " + quantity + " in stock.");
				System.out.println(storeName);
				// Match type M with part number and at least the requested quantity
				// Match type P with part number, color and at least the requested quantity
				if ((searchPart.getType().equalsIgnoreCase("M") && partNum.equals(searchPart.getNumber())
						&& (quantity >= searchPart.getQuantity()))
						|| (!searchPart.getType().equalsIgnoreCase("M") && partNum.equals(searchPart.getNumber())
								&& colorNum.equals(searchPart.getColorNum()) && (quantity >= searchPart.getQuantity()))) {
					// Set new StockedPart attributes
					StockedPart currentPart = new StockedPart();
					currentPart.setType(searchPart.getType());
					currentPart.setNumber(partNum);
					currentPart.setName(partName);
					currentPart.setImageURL(imageURL);
					currentPart.setColorNum(colorNum);
					currentPart.setCond(condition);
					currentPart.setCondDescription(conditionDescription);
					currentPart.setPrice(cost);
					currentPart.setCartAddURL(buyURL);
					Vendor currentVendor = new Vendor();
					// System.out.println("About to check dupe vendors to: " + storeName);
					// Check if this Vendor is already in the list by comparing the vendor name.
					if (!vendorList.isEmpty()) {
						System.out.println();
						// If so, store it as currentVendor and remove it from the list while being updated
						for (Vendor tempVendor : vendorList) {
							// System.out.println("Vendor check - |" + tempVendor.getName() + "| does it equal? |" + storeName +
							// "|");
							if (tempVendor.getName().equalsIgnoreCase(storeName)) {
								currentVendor = tempVendor;
								currentPart.setVendor(currentVendor);
							}
						}
					}
					// System.out.println("About to check dupe part against :" + currentVendor.getMatchingParts());
					boolean foundAlready = false;
					// Some vendors have multiple entries for the same part. If this is a duplicate, then skip adding what is
					// foundAlready.
					if (!currentVendor.getMatchingParts().isEmpty()) {
						for (StockedPart tempStock : currentVendor.getMatchingParts()) {
							// System.out.println("Vendor " + currentVendor.getName() + " has - |" + tempStock.getNumber() + "|
							// does it equal? |" + currentPart.getNumber() + "|");
							if (tempStock.getNumber().equalsIgnoreCase(currentPart.getNumber())) {
								foundAlready = true;
							}
						}
					}
					// This part has been added to the vendor list already, so skip it.
					if (!foundAlready) {
						vendorList.remove(currentVendor);
						currentVendor.setOurCustomer(searchPart.getMyCustomer());
						currentVendor.setName(storeName);
						currentVendor.setLink(storeURL);
						currentVendor.setMinBuy(minBuy);
						List<StockedPart> tempMatches = currentVendor.getMatchingParts();
						tempMatches.add(currentPart);
						currentVendor.setMatchingParts(tempMatches);
						currentPart.setVendor(currentVendor);

						vendorList.add(currentVendor);
						resultsFound++;
						foundFirst = true;
					}
				} else if (foundFirst) {
					lastPage = index;
					System.out.println("Part number no longer matches on page " + page + " in row " + (index));
					break outerLoop;
				}
			}
			System.out.println("Page " + page + " url is " + url);
		}
		System.out.println("totalPages = " + lastPage);
		System.out.println("Found the part at " + resultsFound + " stores.");
		if (resultsFound == 0) {
			System.out.println("Could not find vendors selling: " + searchPart);
			return new ArrayList<Vendor>();
		}
		return vendorList;
	}

	public Vendor findVendorLoc(Vendor searchVendor) {

		String vendorURL = searchVendor.getLink();
		System.out.println(vendorURL);
		String termsUrl = vendorURL + "#/terms";
		System.out.println("++++++findVendorLoc - termsURL is " + termsUrl);
		Document doc = null;
		try {
			doc = Jsoup.connect(termsUrl).get();
			// Document doc = Jsoup.parse(html);
		} catch (Exception ex) {
			System.out.println("IO error connecting to " + termsUrl);
			ex.printStackTrace();
			return null;
		}
		if (!doc.getElementsContainingText("Please try again").isEmpty()) {
			System.out.println("++++++findVendorLoc - invalid vendor URL " + vendorURL);
			return null;
		}
		Elements spanElements = doc.select("span");
		String stateCountry = spanElements.get(14).text();
		System.out.println("++++++findVendorLoc - stateCountry is " + stateCountry);
		String[] stateCountryArray = stateCountry.split(", ");
		String state = stateCountryArray[0].trim();
		String country = "";
		if (stateCountryArray.length > 1)
			country = stateCountry.split(", ")[1].trim();
		if (country.equalsIgnoreCase("USA"))
			country = "US";
		if (country.equalsIgnoreCase("Canada"))
			country = "CA";
		System.out.println("++++++findVendorLoc - invalid information in stateCountry " + stateCountry);
		searchVendor.setLocation(state + ", " + country);
		searchVendor.setTerms(termsUrl);
		// System.out.println("State is " + state);
		// System.out.println("url for the vendor terms is " + termsUrl);
		return searchVendor;
	}

}
