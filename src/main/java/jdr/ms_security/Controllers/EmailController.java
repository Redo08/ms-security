package jdr.ms_security.Controllers;

import jdr.ms_security.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("api/send-email")
public class EmailController {
    @Autowired
    private EmailService theEmailService;


//    @PostMapping
//    public void Email() {
//        theEmailService.sendEmail();
//        System.out.println("Se envió el correo");
//    }

}
