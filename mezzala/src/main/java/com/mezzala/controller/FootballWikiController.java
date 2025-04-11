package com.mezzala.controller;

import com.mezzala.dto.BoardDto;
import com.mezzala.dto.BoardSmallCategoryDto;
import com.mezzala.dto.UserDto;
import com.mezzala.service.NormalhubService;
import com.mezzala.ui.ThePager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = {"footballWiki"})
public class FootballWikiController {

    @Setter(onMethod_ = {@Autowired})
    private NormalhubService normalhubService;

    @GetMapping(path = {"/home"})
    public String home() {
        return "/footballWiki/footballhubHome";
    }

}
