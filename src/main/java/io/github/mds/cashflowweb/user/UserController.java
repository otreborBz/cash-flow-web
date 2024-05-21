package io.github.mds.cashflowweb.user;

import io.github.mds.cashflowweb.employee.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/api/auth")
    public ResponseEntity<?> isAuthenticated(@AuthenticationPrincipal Employee employee) {
        return ResponseEntity.noContent().build();
    }

}
