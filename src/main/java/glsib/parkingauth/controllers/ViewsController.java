package glsib.parkingauth.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Controller
public class ViewsController {
    @GetMapping("/landing")
    public String showLandingPage() {
        return "landing";
    }
    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

}
