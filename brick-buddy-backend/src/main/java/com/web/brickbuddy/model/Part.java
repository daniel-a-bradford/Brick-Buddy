package com.web.brickbuddy.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Part {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="part_id")
	private long partID;
	private String type = "";
	private String number = "";
	private String name = "";
	private String colorNum = "";
	private String imageURL = "";
	private String cond = ""; //attribute name "condition" causes an error creating database table
	private int quantity = 0;
	
	public long getPartID() {
		return this.partID;
	}

	public void setPartID(long partID) {
		this.partID = partID;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColorNum() {
		return this.colorNum;
	}
	
	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}
	
	public String getImageURL() {
		return this.imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getCond() {
		return this.cond;
	}
	
	public void setCond(String condition) {
		this.cond = condition;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean validatePart() {
		StringChecker check = new StringChecker(true);
		if (!check.isValidString(this.type, 1)) {
			System.out.println("validatePart - Invalid type");
			return false;
		}
		if (!check.isValidString(this.number)) {
			System.out.println("validatePart - Invalid part number");
			return false;
		}
		if (!check.isValidString(this.colorNum)) {
			System.out.println("validatePart - Invalid color number");
			return false;
		}
		if (!check.isValidString(this.name)) {
			System.out.println("validatePart - Invalid name");
			return false;
		}
		if (!check.isValidString(this.imageURL)) {
			System.out.println("validatePart - Invalid imageURL");
			return false;
		}
		System.out.println("validatePart - Part " + this.partID + " is valid");
		return true;
	}

	@Override
	public String toString() {
		return "Part [partID=" + this.partID + ", type=" + this.type + ", number=" + this.number + ", name=" + this.name
				+ ", colorNum=" + this.colorNum	+ ", imageURL=" + this.imageURL + ", cond=" + this.cond + ", quantity=" + this.quantity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + ((this.colorNum == null) ? 0 : this.colorNum.hashCode());
		result = prime * result + ((this.cond == null) ? 0 : this.cond.hashCode());
		result = prime * result + ((this.imageURL == null) ? 0 : this.imageURL.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.number == null) ? 0 : this.number.hashCode());
		result = prime * result + (int) (this.partID ^ (this.partID >>> 32));
		result = prime * result + this.quantity;
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
		Part other = (Part) obj;
		if (this.colorNum == null) {
			if (other.colorNum != null)
				return false;
		} else if (!this.colorNum.equals(other.colorNum))
			return false;
		if (this.cond == null) {
			if (other.cond != null)
				return false;
		} else if (!this.cond.equals(other.cond))
			return false;
		if (this.imageURL == null) {
			if (other.imageURL != null)
				return false;
		} else if (!this.imageURL.equals(other.imageURL))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.number == null) {
			if (other.number != null)
				return false;
		} else if (!this.number.equals(other.number))
			return false;
		if (this.partID != other.partID)
			return false;
		if (this.quantity != other.quantity)
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		return true;
	}

}
