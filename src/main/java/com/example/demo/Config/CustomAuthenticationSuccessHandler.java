package com.example.demo.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Lấy ra vai trò của người dùng từ đối tượng Authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                // Nếu người dùng có vai trò ADMIN, thiết lập url mục tiêu là indexadmin
                try {
                    response.sendRedirect("/indexadmin");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                // Nếu người dùng có vai trò USER, thiết lập url mục tiêu là indexuser
                try {
                    response.sendRedirect("indexuser");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Gọi phương thức của lớp cha để thực hiện chuyển hướng
        super.onAuthenticationSuccess(request, response, authentication);
    }
}