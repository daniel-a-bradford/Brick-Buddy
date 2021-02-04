package com.web.brickbuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="vendors")
@EntityListeners(AuditingEntityListener.class)
public class Vendor implements Serializable {
	
	@Transient
	private static final long serialVersionUID = -1170998517110197567L;

	@Transient
	private BigDecimal defaultPrice = new BigDecimal("0.00");
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="vendor_id")
	private long vendorID;
	
	@LastModifiedDate
	private LocalDateTime lastUpdated;
	private String name = "";
	private String location = "";
	private String link = "";
	private BigDecimal minBuy = defaultPrice;
	private String terms = "";
	private int matches = 0;
	private BigDecimal partsCost = defaultPrice;
	private BigDecimal shipCost = defaultPrice;
	private BigDecimal addedCost = defaultPrice;
	private BigDecimal estTotal = defaultPrice;
	
	@ManyToOne(cascade=CascadeType.DETACH) 
	@JoinColumn(name="cust_id")
	@JsonBackReference
	private Customer ourCustomer;
	
	public LocalDateTime getLastUpdated() {
		return this.lastUpdated;
	}

	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<StockedPart> matchingParts = new ArrayList<StockedPart>();
	
	public BigDecimal getDefaultPrice() {
		return this.defaultPrice;
	}
	
	public long getVendorID() {
		return this.vendorID;
	}
	
	public void setVendorId(long vendorID) {
		this.vendorID = vendorID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public BigDecimal getMinBuy() {
		return this.minBuy;
	}
	
	public void setMinBuy(BigDecimal minBuy) {
		this.minBuy = minBuy;
	}
	
	public String getTerms() {
		return this.terms;
	}
	
	public void setTerms(String terms) {
		this.terms = terms;
	}
	
	public int getMatches() {
		this.matches = this.matchingParts.size();
		return this.matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public BigDecimal getPartsCost() {
		BigDecimal partsCost = this.defaultPrice;
		if (this.matchingParts.size() == 0) return partsCost;
		for (StockedPart tempPart : this.matchingParts) {
			partsCost = partsCost.add(tempPart.getPrice());
		}
		this.partsCost = partsCost.round(new MathContext(2));
		return this.partsCost;
	}
	
	public void setPartsCost(BigDecimal partsCost) {
		partsCost = partsCost.round(new MathContext(2));
		if (partsCost.compareTo(this.defaultPrice) > 0) {
			this.partsCost = partsCost;
		}
	}
	
	public BigDecimal getShipCost() {
		return this.shipCost.round(new MathContext(2));
	}
	
	public void setShipCost(BigDecimal shipCost) {
		shipCost = shipCost.round(new MathContext(2));
		this.estTotal = this.shipCost.add(this.addedCost);
		this.shipCost = shipCost;
	}
	
	public BigDecimal getAddedCost() {
		return this.addedCost.round(new MathContext(2));
	}
	
	public void setAddedCost(BigDecimal addedCost) {
		addedCost = addedCost.round(new MathContext(2));
		this.estTotal = this.shipCost.add(this.addedCost);
		this.addedCost = addedCost;
	}
	
	public BigDecimal getEstTotal() {
		// If the minimum buy is less than the prices of the pieces themselves, the estimated total is minBuy plus shipCost plus addedCost.
		if (this.getPartsCost().compareTo(this.minBuy) < 0) {
			this.estTotal = this.minBuy.add(this.getShipCost().add(this.getAddedCost()));
		} else this.estTotal = this.getPartsCost().add(this.getShipCost().add(this.getAddedCost()));
		
		return this.estTotal.round(new MathContext(2));
	}
	
	public void setEstTotal(BigDecimal estTotal) {
		addedCost = addedCost.round(new MathContext(2));
		this.estTotal = estTotal;
	}

	
//	public Set<WantedPart> getWantedParts() {
//		return this.wantedParts;
//	}
//
//	public void setWantedParts(Set<WantedPart> wantedParts) {
//		this.wantedParts = wantedParts;
//	}

	public Customer getOurCustomer() {
		return this.ourCustomer;
	}

	public void setOurCustomer(Customer ourCustomer) {
		this.ourCustomer = ourCustomer;
	}

	public List<StockedPart> getMatchingParts() {
		return this.matchingParts;
	}

	public void setMatchingParts(List<StockedPart> matchingParts) {
		this.matchingParts = matchingParts;
	}

	@Override
	public String toString() {
		String returnString = "Vendor [vendorID=" + this.vendorID + ", lastUpdated=" + this.lastUpdated + ", name=" + this.name + ", location="
				+ this.location + ", link=" + this.link + ", minBuy=" + this.minBuy + ", terms="
				+ this.terms + ", matches=" + this.matches + ", partsCost=" + this.partsCost + ", shipCost=" + this.shipCost
				+ ", addedCost=" + this.addedCost + ", estTotal=" + this.estTotal + ", ourCustomer=";
			if (this.ourCustomer == null) returnString += "null,";
			else returnString += this.ourCustomer;
		return returnString + ", matchingParts=" + this.matchingParts + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + ((this.addedCost == null) ? 0 : this.addedCost.hashCode());
		result = prime * result + ((this.estTotal == null) ? 0 : this.estTotal.hashCode());
		result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
		result = prime * result + ((this.link == null) ? 0 : this.link.hashCode());
		result = prime * result + this.matches;
		result = prime * result + ((this.matchingParts == null) ? 0 : this.matchingParts.hashCode());
		result = prime * result + ((this.minBuy == null) ? 0 : this.minBuy.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.ourCustomer == null) ? 0 : (int)this.ourCustomer.getCustomerID());
		result = prime * result + ((this.partsCost == null) ? 0 : this.partsCost.hashCode());
		result = prime * result + ((this.shipCost == null) ? 0 : this.shipCost.hashCode());
		result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result + ((this.terms == null) ? 0 : this.terms.hashCode());
		result = prime * result + (int) (this.vendorID ^ (this.vendorID >>> 32));
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
		Vendor other = (Vendor) obj;
		if (this.addedCost == null) {
			if (other.addedCost != null)
				return false;
		} else if (!this.addedCost.equals(other.addedCost))
			return false;
		if (this.estTotal == null) {
			if (other.estTotal != null)
				return false;
		} else if (!this.estTotal.equals(other.estTotal))
			return false;
		if (this.lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!this.lastUpdated.equals(other.lastUpdated))
			return false;
		if (this.link == null) {
			if (other.link != null)
				return false;
		} else if (!this.link.equals(other.link))
			return false;
		if (this.matches != other.matches)
			return false;
		if (this.matchingParts == null) {
			if (other.matchingParts != null)
				return false;
		} else if (!this.matchingParts.equals(other.matchingParts))
			return false;
		if (this.minBuy == null) {
			if (other.minBuy != null)
				return false;
		} else if (!this.minBuy.equals(other.minBuy))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.ourCustomer == null) {
			if (other.ourCustomer != null)
				return false;
		} else if (this.ourCustomer.getCustomerID() != other.ourCustomer.getCustomerID())
			return false;
		if (this.partsCost == null) {
			if (other.partsCost != null)
				return false;
		} else if (!this.partsCost.equals(other.partsCost))
			return false;
		if (this.shipCost == null) {
			if (other.shipCost != null)
				return false;
		} else if (!this.shipCost.equals(other.shipCost))
			return false;
		if (this.location == null) {
			if (other.location != null)
				return false;
		} else if (!this.location.equals(other.location))
			return false;
		if (this.terms == null) {
			if (other.terms != null)
				return false;
		} else if (!this.terms.equals(other.terms))
			return false;
		if (this.vendorID != other.vendorID)
			return false;
		return true;
	}
	
}
