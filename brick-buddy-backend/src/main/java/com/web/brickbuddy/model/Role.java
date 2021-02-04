package com.web.brickbuddy.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role implements Serializable {	
	@Transient
	private static final long serialVersionUID = 2422361421904660449L;

	public Role() {
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private long id;
   
	@Column(name = "role")
    private String role;   

	public Role(long id, String role) {
		this.id = id;
		this.role = role;
	}

	public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    

	@Override
	public String toString() {
		return "Role [id=" + this.id + ", role=" + this.role + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 7;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		result = prime * result + ((this.role == null) ? 0 : this.role.hashCode());
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
		Role other = (Role) obj;
		if (this.id != other.id)
			return false;
		if (this.role == null) {
			if (other.role != null)
				return false;
		} else if (!this.role.equals(other.role))
			return false;
		return true;
	}

}
