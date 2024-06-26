package com.example.FF_FastFood.controller;

import com.example.FF_FastFood.entity.Account;
import com.example.FF_FastFood.entity.Customer;
import com.example.FF_FastFood.model.ForgotPasswordViewModel;
import com.example.FF_FastFood.model.LoginViewModel;
import com.example.FF_FastFood.model.RegisterViewModel;
import com.example.FF_FastFood.model.ResetPasswordViewModel;
import com.example.FF_FastFood.model.VerifyResetCodeViewModel;
import com.example.FF_FastFood.service.AccountService;
import com.example.FF_FastFood.service.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    // GET: /account/login
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginViewModel", new LoginViewModel());
        return "Account/login"; // return view name
    }

    // POST: /account/login
    @PostMapping("/login")
    public String loginSubmit(@Valid @ModelAttribute("loginViewModel") LoginViewModel model,
                              BindingResult bindingResult, HttpServletRequest request,
                              HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("Sai tên đăng nhập hoặc mật khẩu!");
            return "Account/login"; // return to login page if validation fails
        }

        Account user = accountService.authenticateUser(model.getUsername(), model.getPassword());

        if (user != null) {
            Customer customer = accountService.findCustomerByAccountId(user.getId());
            // Login success
            String encodedName = URLEncoder.encode(customer.getName(), StandardCharsets.UTF_8);
            Cookie userCookie = new Cookie("UserCookie", encodedName);
            userCookie.setValue(customer.getAccount().getId().toString());
            userCookie.setMaxAge(3600); // Cookie expires in 1 hour
            userCookie.setPath("/");
            response.addCookie(userCookie);
            return "redirect:/home"; // redirect to food page
        } else {
            bindingResult.reject("", "Invalid username or password.");
            return "Account/login"; // return to login page with error
        }
    }

    // GET: /account/signup
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("registerViewModel", new RegisterViewModel());
        return "Account/signup"; // return view name
    }

    // POST: /account/signup
    @PostMapping("/signup")
    public String signupSubmit(@Valid @ModelAttribute("registerViewModel") RegisterViewModel model,
                               BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "Account/signup"; // return to signup page if validation fails
        }

        // Check if username exists
        if (accountService.existsByUsername(model.getUsername())) {
            bindingResult.rejectValue("userName", "", "Phone number already exists.");
            return "Account/signup";
        }

        // Create new account
        Account newAccount = new Account();
        newAccount.setUsername(model.getUsername());
        newAccount.setPassword(accountService.hashPassword(model.getPassword())); // Hash password
        newAccount.setRole("customer");
        newAccount.setCreatedAt(new Date());
        newAccount.setUpdatedAt(new Date());

        accountService.saveAccount(newAccount);

        // Create new customer
        Customer newCustomer = new Customer();
        newCustomer.setAccount(newAccount);
        newCustomer.setName(model.getName());
        newCustomer.setPhone(model.getPhone());
        newCustomer.setCreatedAt(new Date());
        newCustomer.setUpdatedAt(new Date());

        accountService.saveCustomer(newCustomer);

        return "redirect:/account/login"; // redirect to login page after successful signup
    }

    // GET: /account/logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear session or cookie
        Cookie userCookie = new Cookie("UserCookie", "");
        userCookie.setMaxAge(0); // Clear cookie
        response.addCookie(userCookie);
        return "redirect:/account/login"; // redirect to login page
    }

    // GET: /account/forgotpassword
    @GetMapping("/forgotpassword")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordViewModel", new ForgotPasswordViewModel());
        return "Account/forgot-password"; // return view name
    }

    // POST: /account/forgotpassword
    @PostMapping("/forgotpassword")
    public String forgotPasswordSubmit(@Valid @ModelAttribute("forgotPasswordViewModel") ForgotPasswordViewModel model,
                                       BindingResult bindingResult, HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/account/forgotpassword"; // return to forgotpassword page if validation fails
        }

        Customer customer = accountService.findCustomerByEmail(model.getEmail());

        if (customer != null) {
            // Generate reset code and send email
            String resetCode = generateResetCode();
            customer.getAccount().setPasswordResetCode(resetCode);
            customer.getAccount().setResetCodeExpiration(new Date(System.currentTimeMillis() + 3600000)); // Expire in 1 hour

            accountService.saveAccount(customer.getAccount());

            // Send reset password email


            String emailMessage = "Your Reset Password Code\n" + resetCode;
            emailService.sendResetCodeEmail(customer.getEmail(), emailMessage);
            return "redirect:/account/verifyresetcode?email=" + customer.getEmail() + "&resetCode=" + resetCode;

        } else {
            bindingResult.reject("", "Email not found.");
        }

        return "redirect:/account/forgotpassword"; // redirect to forgotpassword page
    }

    // GET: /account/resetpassword
    @GetMapping("/verifyresetcode")
    public String resetPasswordForm(@RequestParam("email") String email, @RequestParam("resetCode") String resetCode,
                                    Model model) {
        VerifyResetCodeViewModel viewModel = new VerifyResetCodeViewModel();
        viewModel.setEmail(email);
        viewModel.setResetCode(resetCode);
        model.addAttribute("verifyResetCodeViewModel", viewModel);
        return "Account/verify-reset-code"; // return view name
    }

    // POST: /account/resetpassword
    @PostMapping("/verifyresetcode1")
    public String resetPasswordSubmit(VerifyResetCodeViewModel model,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Customer user = accountService.findCustomerByEmail(model.getEmail());
        if (user == null) {
            return "redirect:/account/login";
        }
        Account account = user.getAccount();

        // So sánh mã reset code
        if (!model.getResetCode().equals(account.getPasswordResetCode())) {
            return "redirect:/account/login";
        }

        // Chuyển hướng đến trang đặt lại mật khẩu
        redirectAttributes.addAttribute("email", user.getEmail());
        return "redirect:/account/resetpassword";
    }


    @GetMapping("/resetpassword")
    public String resetPasswordForm(@RequestParam("email") String email, Model model) {
        ResetPasswordViewModel viewModel = new ResetPasswordViewModel();
        viewModel.setEmail(email);
        model.addAttribute("resetPasswordViewModel", viewModel);
        return "Account/reset-password";
    }


    @PostMapping("/resetpassword")
    public String resetPasswordSubmit(@Valid ResetPasswordViewModel viewModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "resetpassword";
        }

        Customer user = accountService.findCustomerByEmail(viewModel.getEmail());
        if (user == null) {
            model.addAttribute("errorMessage", "Người dùng không tồn tại.");
            return "resetpassword";
        }

        Account account = accountService.findCustomerByAccountId(user.getAccount().getId()).getAccount();
        if (account == null) {
            model.addAttribute("errorMessage", "Tài khoản không tồn tại.");
            return "resetpassword";
        }

        String newPassword = viewModel.getNewPassword();
        if (newPassword == null) {
            model.addAttribute("errorMessage", "Mật khẩu mới không được để trống.");
            return "redirect:/account/login";
        }
        // Mã hóa mật khẩu mới
        account.setPassword(bCryptPasswordEncoder.encode(viewModel.getNewPassword()));
        account.setPasswordResetCode(null);
        account.setResetCodeExpiration(null);
        accountService.saveAccount(account);

        return "redirect:/food/index";
    }
    // Helper method to generate a random reset code
    private String generateResetCode() {
        Random random = new Random();
        int resetCode = 100000 + random.nextInt(900000); // Generate a 6-digit random number
        return String.valueOf(resetCode);
    }
}
