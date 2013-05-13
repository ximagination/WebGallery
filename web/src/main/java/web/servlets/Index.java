package web.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: agnidash
 * Date: 5/8/13
 * Time: 4:08 PM
 */
@Controller
@RequestMapping("/")
public class Index {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String toMainPage() throws Throwable {
        return "redirect:/Login";
    }
}
