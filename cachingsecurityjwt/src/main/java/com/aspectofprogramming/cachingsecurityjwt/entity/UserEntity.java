package com.aspectofprogramming.cachingsecurityjwt.entity;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity implements UserDetails {

//    private static final long serialVersionUID = 1L; // Recommended for versioning , java.io.Serializable

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user's roles. Example:
        return List.of(new SimpleGrantedAuthority(this.role));
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    // Implement other required methods
    @Override
    public String getPassword() { 
        return this.password; 
    }
    @Override 
    public String getUsername() { 
        return this.username; 
    }
    @Override 
    public boolean isAccountNonExpired() { 
        return true; 
    }
    @Override 
    public boolean isAccountNonLocked() { 
        return true; 
    }
    @Override
    @JsonIgnore // not going to display in Json Response
    public boolean isCredentialsNonExpired() { 
        return true; 
    }
    @Override 
    public boolean isEnabled() { 
        return true; 
    }

}
