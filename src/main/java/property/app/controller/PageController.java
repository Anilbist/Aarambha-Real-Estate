/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package property.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author SUBASH
 */
@Controller
public class PageController {
    @GetMapping("/about")
    public String aboutUs(){
    return "/layouts/about";
    }
   

    @GetMapping("/contact")
    public String Contact(){
    return "/layouts/contact";
    }
}
