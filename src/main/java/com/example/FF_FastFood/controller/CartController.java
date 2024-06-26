package com.example.FF_FastFood.controller;

import com.example.FF_FastFood.service.AccountService;
import com.example.FF_FastFood.entity.Cart;
import com.example.FF_FastFood.service.AccountServiceImpl;
import com.example.FF_FastFood.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long foodId, HttpServletRequest request) {
        Long customerId = getCustomerIdFromCookie(request);
        if (customerId == null) {
            return "User is not logged in, please login to continue!";
        }

        cartService.addToCart(foodId, customerId);
        return "Item added to cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateQuantity(@RequestParam Long cartItemId, @RequestParam String changeQuantity) {
        cartService.updateQuantity(cartItemId, changeQuantity);
        return "Cart item quantity updated";
    }

    @PostMapping("/remove")
    @ResponseBody
    public String removeItem(@RequestParam Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "Item removed from cart";
    }
    @Autowired
    private AccountService accountService;
    @GetMapping
    public String getCart(Model model, HttpServletRequest request) {
        Long accountId = getCustomerIdFromCookie(request);
        if (accountId == null) {
            return "redirect:/account/login";
        }
        var cus = accountService.findCustomerByAccountId(accountId);
        Cart cart = cartService.getCartByCustomerId(cus.getCustomerId());
        model.addAttribute("cart", cart);
        return "Cart/index";
    }

    private Long getCustomerIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserCookie".equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    try {
                        return Long.valueOf(cookieValue);
                    } catch (NumberFormatException e) {
                        // Handle parsing error (e.g., log it or return null)
                        e.printStackTrace();
                        return null; // or handle in a different way
                    }
                }
            }
        }
        return null;
    }

}
