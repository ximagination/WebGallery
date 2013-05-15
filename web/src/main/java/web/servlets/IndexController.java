package web.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import web.pojo.Login;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static web.utils.JSPUtils.DEFAULT_PAGE;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/8/13
 * Time: 4:08 PM
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = {GET, POST})
    public String toMainPage(Model model) {
        model.addAttribute("login", new Login());
        return DEFAULT_PAGE;
    }
}
