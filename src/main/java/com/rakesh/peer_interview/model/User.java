package com.rakesh.peer_interview.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
	
	@Column(name = "username", unique = true, length = 200)
    private String username;

    @Column(name = "password", length = 500)
    private String password;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    
	public User() {}

	public User(Long id, String username, String password, Boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
    public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", enabled=" + enabled + "]";
	}

}
