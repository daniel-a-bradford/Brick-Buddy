package com.web.brickbuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="wanted_parts") // Use underscores, not hyphens or it will cause an exception trying to make the database table.
public class WantedPart extends Part implements Serializable {

	@Transient
	private static final long serialVersionUID = -2606667622084005590L;
	
	@ManyToOne(cascade=CascadeType.DETACH) 
	@JoinColumn(name="cust_id")
	@JsonBackReference
	private Customer myCustomer;
	
//	@ManyToMany(cascade = CascadeType.DETACH)
//	@JoinTable(name = "matching_vendors", joinColumns = @JoinColumn(name = "part_id"),
//	inverseJoinColumns = @JoinColumn(name = "vendor_id"))
////	@JsonManagedReference needed for OneToMany
//	private Set<Vendor> matchingVendors = new HashSet<Vendor>();

	public Customer getMyCustomer() {
		return this.myCustomer;
	}

	public void setMyCustomer(Customer myCustomer) {
		this.myCustomer = myCustomer;
	}

//	public Set<Vendor> getMatchingVendors() {
//		return this.matchingVendors;
//	}
//
//	public void setMatchingVendors(Set<Vendor> matchingVendors) {
//		this.matchingVendors = matchingVendors;
//	}
//	
//	private ArrayList<Long> makeIdList() {
//		ArrayList<Long> idList = new ArrayList<Long>();
//		for (Vendor tempPart : this.matchingVendors) {
//			idList.add(tempPart.getVendorID());
//		}
//		return idList;
//	}

	@Override
	public String toString() {
		String returnString = "WantedPart [" + super.toString() + ", customer=";
		if (this.myCustomer == null) returnString += "null";
		else returnString += this.myCustomer.getCustomerID();
		returnString += ", has matchingVendors=";
//		if (this.matchingVendors == null) returnString += "null]"; 
//		else returnString += makeIdList();
		return returnString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.myCustomer == null) ? 0 : (int) this.myCustomer.getCustomerID());
//		result = prime * result + ((this.matchingVendors == null) ? 0 : makeIdList().hashCode());
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
		WantedPart other = (WantedPart) obj;
		if (this.myCustomer == null) {
			if (other.myCustomer != null)
				return false;
		} else if (this.myCustomer.getCustomerID() != other.myCustomer.getCustomerID())
			return false;
//		if (this.matchingVendors == null) {
//			if (other.matchingVendors != null)
//				return false;
//		} else if (!this.makeIdList().equals(other.makeIdList()))
//			return false;
		return true;
	}

}
