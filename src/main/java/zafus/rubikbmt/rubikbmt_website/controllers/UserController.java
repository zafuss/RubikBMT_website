package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateUser;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateUser;
import zafus.rubikbmt.rubikbmt_website.services.RoleService;
import zafus.rubikbmt.rubikbmt_website.services.UserService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {
//    @Value("${recaptcha.secret}")
//    private String recaptchaSecret;
//
//    @Value("${recaptcha.url}")
//    private String recaptchaServerURL;

//    @Bean
//    public static RestTemplate restTemplate(RestTemplateBuilder bulider) {
//        return bulider.build();
//    }

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private PasswordEncoder passwordEncoder;
    @GetMapping("/users")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(
//                Sort.Order.asc("isConfirmed"),
//                Sort.Order.desc("registrationTime")
//        ));
        Page<User> userPage = userService.searchUsers(keyword, searchType, pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "user/index";
    }

    @GetMapping("/user")
    public String displayAllUser(Model model) {
        List<User> users = userService.getAllUser();
        System.out.println(users);
        model.addAttribute("list_user", users);
        return "user/index";
    }

    @GetMapping("/users/add")
    public String showFormAdd(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", new RequestCreateUser());

        return "user/add";
    }


    @PostMapping("/users/add")
    public String addUser(@ModelAttribute RequestCreateUser user, @RequestParam List<String> roles) {
        Set<Role> selectedRoles = new HashSet<>();
        for (String roleId : roles) {
            Role role = roleService.findById(roleId);
            if (role != null) {
                selectedRoles.add(role);
            }
        }
        user.setRoles(selectedRoles);
        userService.save(user);
        return "redirect:/users";
    }



    @PostMapping("/users/edit")
    public String edit(@ModelAttribute RequestUpdateUser user, @RequestParam List<String> roles) {
        Set<Role> selectedRoles = new HashSet<>();
        for (String roleId : roles) {
            Role role = roleService.findById(roleId);
            if (role != null) {
                selectedRoles.add(role);
            }
        }
        user.setRoles(selectedRoles);
        userService.update(user);
        return "redirect:/users/detail?id=" + user.getId();
    }
    @GetMapping("/users/detail")
    public String detail(@RequestParam("id") String id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/detail";
    }
    @PostMapping("/user/add_new")
    public String addNewUser(@ModelAttribute("user") RequestCreateUser requesUser,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", "Thêm tài khoản thành công");
        return "redirect:/user";
    }

    @GetMapping("/users/edit")
    public String edit(@RequestParam("id") String id,
                       Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        return "user/edit";
    }

    //    @PostMapping("/user/save_change")
//    public String saveChange(RequestUpdateUser requestUpdateUser,
//                             @RequestParam("photo") MultipartFile multipartFile,
//                             RedirectAttributes redirectAttributes) {
//        userService.UpdateUser(requestUpdateUser, multipartFile);
//        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
//        return "redirect:/user";
//
//    }
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RequestCreateUser());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RequestCreateUser requestUser, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String gRecaptchaReponse = request.getParameter("g-recaptcha-response");
//
//        if(!verifyReCAPCHA(gRecaptchaReponse)) {
//            response.reset();
//            String errorMessage1 = "Please Verify captcha response";
//            model.addAttribute("errorsVerify", errorMessage1);
//            return "user/register";
//        }
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "auth/register";
        }
        userService.save(requestUser);
        userService.setDefaultRole(requestUser.getUserName());
        return "redirect:/login";
    }

//    @GetMapping("/user/profile")
//    public String profile(@AuthenticationPrincipal User user1,
//                          @AuthenticationPrincipal CustomOAuth2User user2,
//                          Model model,
//                          HttpSession session) {
//        User user;
//        if (user1==null)
//            user = userService.findByEmail(user2.getEmail());
//        else
//            user = userService.findById(user1.getId());
//        model.addAttribute("user", user);
//        return "/user/profile";
//    }

//    @PostMapping("/user/save_profile")
//    public String saveProfile(@Valid RequestUpdateUser requestUpdateUser,
//                              BindingResult bindingResult,
//                              @RequestParam("photo") MultipartFile multipartFile,
//                              RedirectAttributes redirectAttributes,
//                              HttpSession session,
//                              Model model) {
//        User user = (User) session.getAttribute("userLogin");
//        if (bindingResult.hasErrors() ) {
//            List<String> customErrors = new ArrayList<>();
//            List<String> errors = bindingResult.getAllErrors()
//                    .stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
//
//            if (!user.getPhoneNumber().equals(requestUpdateUser.getPhoneNumber())){
//                User tmpUser = userService.findByPhone(requestUpdateUser.getPhoneNumber());
//                if (tmpUser != null && !tmpUser.getId().equals(user.getId())){
//                    customErrors.add("Phone Đã Tồn Tại");
//                }
//            }
//            List<String> combinedErrors = new ArrayList<>(customErrors);
//            combinedErrors.addAll(errors);
//            model.addAttribute("errors", combinedErrors);
//            model.addAttribute("user", user);
//            return "user/profile";
//        }
//        User user1 = userService.UpdateUser(requestUpdateUser, multipartFile);
//        UserDetails userDetails = userService.loadUserByUsername(user1.getUsername());
//        userService.updatePrincipal(user1);
//        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
//        return "redirect:/user/profile";
//    }

//    @GetMapping("/user/change_password")
//    public String ChangePassword(@AuthenticationPrincipal User user,
//                                 Model model) {
//        RequestChangePassUser requestChangePassUser = new RequestChangePassUser();
//        requestChangePassUser.setId(user.getId());
//        model.addAttribute("user", requestChangePassUser);
//        return "/user/change_password";
//    }
//
//    @PostMapping("/user/save_change_password")
//    public String ChangePassword_Submit(@AuthenticationPrincipal User user,
//                                        @ModelAttribute("user") RequestChangePassUser requestChangePassUser,
//                                        RedirectAttributes redirectAttributes, Model model) {
//        try {
//            boolean checkpass = passwordEncoder.matches(requestChangePassUser.getOldPassword(), user.getPasswordHash());
//            if (checkpass) {
//                userService.ChangePassword(user, requestChangePassUser.getNewPassword());
//                model.addAttribute("successMessage", "Cập nhật thành công");
//                return "home/index";
//            } else {
//                redirectAttributes.addFlashAttribute("errormessage", " Mật khẩu cũ không hợp lệ");
//                return "redirect:/user/change_password";
//            }
//        } catch (Exception e) {
//            return "home/index";
//        }
//    }

//    @GetMapping("/forgotpassword")
//    public String ForgotPassword() {
//        return "/user/forgot_password";
//    }

//    @PostMapping("/forgot_password")
//    public String ForgotPassword_Submit(@RequestParam("email") String email,
//                                        RedirectAttributes redirectAttributes,
//                                        Model model) {
//        User user = userService.findUserByEmail(email);
//        if (user != null) {
//            user = userService.createTokenResetPassword(user);
//            String token = user.getResetPasswordToken();
//            String URL = "http://localhost:8080/reset_password?token=" + token;
//            userService.SendMail(user.getEmail(), URL, user.getUsername());
//            model.addAttribute("message", "Vui lòng kiểm tra email để thay đổi mật khẩu!");
//            return "/user/after-sendmail";
//        }
//        redirectAttributes.addFlashAttribute("message", "Email không tồn tại");
//        return "redirect:/forgotpassword";
//    }

//    @GetMapping("/reset_password")
//    public String ResetPassword(@Param("token") String token,
//                                Model model,
//                                RedirectAttributes redirectAttributes) {
//        User user = userService.findUserByResetPasswordToken(token);
//        if (user == null) {
//            return "redirect:/forgotpassword";
//        } else {
//            if (user.getResetPasswordTokenExpired().getTime() > System.currentTimeMillis()) {
//                model.addAttribute("token", token);
//                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
//                return "/user/reset_password";
//            } else {
//                return "/user/fail_token";
//            }
//
//        }
//    }

//    @PostMapping("/reset_password")
//    public String ResetPassword_Submit(@RequestParam("token") String token,
//                                       @RequestParam("password") String password) {
//        User user = userService.findUserByResetPasswordToken(token);
//        if (user == null) {
//            return "redirect:/forgotpassword";
//        } else {
//            userService.updateResetPasswordToken(user, password);
//            return "redirect:/login";
//        }
//    }

    @GetMapping("/user/locker/{id}")
    public String lockAccount(@PathVariable String id,
                              RedirectAttributes redirectAttributes) {
        userService.lockAccount(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "redirect:/user";
    }

    @GetMapping("/user/unlocker/{id}")
    public String unlockAccount(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        userService.unlockAccount(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "redirect:/user";
    }

//    private boolean verifyReCAPCHA(String RecaptchaResponse) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("secret", recaptchaSecret);
//        params.add("response", RecaptchaResponse);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        ReCaptchaResponse response = restTemplate.postForObject(recaptchaServerURL, request , ReCaptchaResponse.class);
//
//        System.out.println("Successfully recaptcha response: " + response.isSuccess());
//        System.out.println("HostName: " + response.getHostname());
//        System.out.println("Challenge: " + response.getChallenge_ts());
//        if(response.getErrorCode() != null) {
//            for (String error : response.getErrorCode()) {
//                System.out.println("\t" + error);
//            }
//        }
//        return response.isSuccess();
//    }
}
