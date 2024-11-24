package kz.aibat.keycloaktest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "item")
public class TestController {

    @GetMapping
    public List<String> getItems() {
        List<String> items = new ArrayList<>();
        items.add("Iphone");
        items.add("Samsung");
        items.add("XIAOMI");
        items.add("Meizu");

        return items;
    }
}
