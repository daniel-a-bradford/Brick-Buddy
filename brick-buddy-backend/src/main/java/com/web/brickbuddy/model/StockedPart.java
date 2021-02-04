package com.web.brickbuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "stocked_parts") // Use underscores, not hyphens or it will cause an exception trying to make the database table.
@JsonIgnoreProperties(value = { "defaultPrice" }, allowGetters = true) // include attributes you do not want in queries.
public class StockedPart extends Part implements Serializable {
	@Transient
	private static final long serialVersionUID = -8199718492378387723L;

	@Transient
	private BigDecimal defaultPrice = new BigDecimal("0.00");
	@Lob
	private String condDescription = "";
	private BigDecimal price = defaultPrice;
	private String cartAddURL = "";

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "vendor_id")
	@JsonBackReference
	private Vendor vendor;

	public Vendor getVendor() {
		return this.vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public BigDecimal getDefaultPrice() {
		return this.defaultPrice;
	}

	public String getCondDescription() {
		return this.condDescription;
	}

	public void setCondDescription(String condDescription) {
		this.condDescription = condDescription;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCartAddURL() {
		return this.cartAddURL;
	}

	public void setCartAddURL(String cartAddURL) {
		this.cartAddURL = cartAddURL;
	}

	public boolean validateStockedPart() {
		if (!this.validatePart()) {
			return false;
		}
		if (this.price == this.defaultPrice) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String returnString = "StockedPart [" + super.toString() + ", condDescription=" + this.condDescription + ", price="
				+ this.price + ", cartAddURL=" + this.cartAddURL + ", vendor=";
		if (this.vendor == null) returnString += "null]";
		else returnString += this.vendor.getVendorID() + "]";
		return returnString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.cartAddURL == null) ? 0 : this.cartAddURL.hashCode());
		result = prime * result + ((this.condDescription == null) ? 0 : this.condDescription.hashCode());
		result = prime * result + ((this.defaultPrice == null) ? 0 : this.defaultPrice.hashCode());
		result = prime * result + ((this.price == null) ? 0 : this.price.hashCode());
		result = prime * result + ((this.vendor == null) ? 0 : (int)this.vendor.getVendorID());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockedPart other = (StockedPart) obj;
		if (this.cartAddURL == null) {
			if (other.cartAddURL != null)
				return false;
		} else if (!this.cartAddURL.equals(other.cartAddURL))
			return false;
		if (this.condDescription == null) {
			if (other.condDescription != null)
				return false;
		} else if (!this.condDescription.equals(other.condDescription))
			return false;
		if (this.defaultPrice == null) {
			if (other.defaultPrice != null)
				return false;
		} else if (!this.defaultPrice.equals(other.defaultPrice))
			return false;
		if (this.price == null) {
			if (other.price != null)
				return false;
		} else if (!this.price.equals(other.price))
			return false;
		if (this.vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (this.vendor.getVendorID() != (other.vendor.getVendorID()))
			return false;
		return true;
	}

}
