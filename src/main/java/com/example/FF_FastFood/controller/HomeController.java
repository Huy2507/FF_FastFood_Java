package com.example.FF_FastFood.controller;

import com.example.FF_FastFood.entity.Banner;
import com.example.FF_FastFood.entity.FoodCategory;
import com.example.FF_FastFood.entity.FoodItem;
import com.example.FF_FastFood.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/home")
    public String index(Model model) {
        List<Banner> banners = foodService.getAllBanners();
        List<FoodCategory> categories = foodService.getAllCategories();
        List<FoodItem> popularFoods = foodService.getPopularFoods();

        model.addAttribute("banners", banners);
        model.addAttribute("categories", categories);
        model.addAttribute("popularFoods", popularFoods);

        return "home/index";
    }
}
