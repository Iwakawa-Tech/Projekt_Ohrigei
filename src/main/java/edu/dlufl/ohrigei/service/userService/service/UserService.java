package edu.dlufl.ohrigei.service.userService.service;

import edu.dlufl.ohrigei.model.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {
    String userSignUp(User user,Model model);
    String userProfile(Model model,String id);
    String userInterviewDetail(Model model, String id, HttpSession session);
}
