package com.web.brickbuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="shipping_locs")
public class ShippingLoc implements Serializable {

	@Transient
	private static final long serialVersionUID = 4551884082923292342L;
	
	@Id // Id is preloaded in the table and does not need to be generated.
	private long state_id;
	private String name = "";
	@Column(name="name_lc")
	private String nameLowerCase = "";
	private String abrv = "";
	private String country = "";
	@Column(name="is_state")
	private boolean usState = false;
	@Column(name="is_lower48")
	private boolean lower48 = false;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private long population = 0;
	private double area = 0.0;
	
	@Transient
	private double baseRate = 2.53;
	@Transient
	private double ratePerMile = 0.37;
	
	public long getState_id() {
		return this.state_id;
	}
	public void setState_id(long state_id) {
		this.state_id = state_id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameLowerCase() {
		return this.nameLowerCase;
	}
	public void setNameLowerCase(String nameLowerCase) {
		this.nameLowerCase = nameLowerCase;
	}
	public String getAbrv() {
		return this.abrv;
	}
	public void setAbrv(String abrv) {
		this.abrv = abrv;
	}
	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean isUsState() {
		return this.usState;
	}
	public void setUsState(boolean usState) {
		this.usState = usState;
	}
	public boolean isLower48() {
		return this.lower48;
	}
	public void setLower48(boolean lower48) {
		this.lower48 = lower48;
	}
	public double getLatitude() {
		return this.latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return this.longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getPopulation() {
		return this.population;
	}
	public void setPopulation(long population) {
		this.population = population;
	}
	public double getArea() {
		return this.area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	
	public double estShipping(ShippingLoc toHere) {
		if (toHere == null) return 0.0; 
		double shipDistance = distance(this.latitude, this.longitude, toHere.getLatitude(), toHere.getLongitude(), "M");
		System.out.println(shipDistance + " miles from " + this.name + " to " + toHere.name);
		double shipCost = baseRate + ratePerMile * (shipDistance/100);
		System.out.println("Estimated ship price is $" + shipCost);
		return shipCost;
	}
	
	// Code to calculate distance used the spherical law of cosines as provided by GeoDataSource.com
	// https://www.geodatasource.com/resources/tutorials/how-to-calculate-the-distance-between-2-locations-using-java/
	private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return (dist);
		}
	}
	
	@Override
	public String toString() {
		return "ShippingLoc [state_id=" + this.state_id + ", name=" + this.name + ", abrv=" + this.abrv + ", country="
				+ this.country + ", usState=" + this.usState + ", lower48=" + this.lower48 + ", latitude=" + this.latitude
				+ ", longitude=" + this.longitude + ", population=" + this.population + ", area=" + this.area + ", baseRate="
				+ this.baseRate + ", ratePerMile=" + this.ratePerMile + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + ((this.abrv == null) ? 0 : this.abrv.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.baseRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
		temp = Double.doubleToLongBits(this.latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (this.lower48 ? 1231 : 1237);
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + (int) (this.population ^ (this.population >>> 32));
		temp = Double.doubleToLongBits(this.ratePerMile);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (this.state_id ^ (this.state_id >>> 32));
		result = prime * result + (this.usState ? 1231 : 1237);
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
		ShippingLoc other = (ShippingLoc) obj;
		if (this.abrv == null) {
			if (other.abrv != null)
				return false;
		} else if (!this.abrv.equals(other.abrv))
			return false;
		if (Double.doubleToLongBits(this.area) != Double.doubleToLongBits(other.area))
			return false;
		if (Double.doubleToLongBits(this.baseRate) != Double.doubleToLongBits(other.baseRate))
			return false;
		if (this.country == null) {
			if (other.country != null)
				return false;
		} else if (!this.country.equals(other.country))
			return false;
		if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (this.lower48 != other.lower48)
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.population != other.population)
			return false;
		if (Double.doubleToLongBits(this.ratePerMile) != Double.doubleToLongBits(other.ratePerMile))
			return false;
		if (this.state_id != other.state_id)
			return false;
		if (this.usState != other.usState)
			return false;
		return true;
	}
	
}
