package com.example.carRetail.serviceImplementation;
import com.example.carRetail.model.CustomUserDetail;
import com.example.carRetail.entity.User;
import com.example.carRetail.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user = this.adminRepository.findByEmail(email);
            System.out.println(user);
            if (user == null) {
                throw new UsernameNotFoundException("NO USER");
            }
            return new CustomUserDetail(user);
        }
}

