package gr.ntua.imu.escapefilterbubble;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class SampleController {

    private static List<String> userList = new ArrayList<String>();

    //Initialize the list with some data for index screen
    static {
        userList.add("Gates");
        userList.add("Jobs");
        userList.add("Larry");
        userList.add("Sergey");

    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String printTest(ModelMap model) {

        model.addAttribute("message", "Hello world- that is my app!");
        return "test";
    }

    @RequestMapping(value = "/ftl", method = RequestMethod.GET)
    public String printFtl(@ModelAttribute("model") ModelMap model) {
        model.addAttribute("userList", userList);
        return "index";
    }
}