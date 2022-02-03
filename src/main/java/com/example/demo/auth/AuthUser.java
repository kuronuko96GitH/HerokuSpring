package com.example.demo.auth;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUser implements UserDetails {

	private static final long serialVersionUID = -4292831594774687625L;

	private String title;

	private long id;

	private String email;

	private Date avf;

	private String username;

	private String password;

	private List<String> roles;

	private boolean locked;

	private boolean expired;



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}


	public Date getAvf() {
		return avf;
	}

	public void setAvf(Date avf) {
		this.avf = avf;
	}



	@Override
	public boolean isAccountNonExpired() {
		return !expired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !expired;
	}

	@Override
	public boolean isEnabled() {
		return !locked;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean getExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}


}