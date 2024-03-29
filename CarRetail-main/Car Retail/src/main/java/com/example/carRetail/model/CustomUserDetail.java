package com.example.carRetail.model;
import com.example.carRetail.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class CustomUserDetail implements UserDetails {

    public CustomUserDetail(User user) {
        this.user = user;
    }

    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> set=new HashSet<>();
        set.add(new SimpleGrantedAuthority(this.user.getRole().getRoleName()));
        System.out.println(this.user.getRole().getRoleName());
        return set;
    }

    @Override
    public String getPassword()
    {
        return this.user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}

