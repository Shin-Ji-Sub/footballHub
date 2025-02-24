package com.mezzala.controller;

import com.mezzala.dto.BoardLargeCategoryDto;
import com.mezzala.service.AccountService;
import com.mezzala.service.BoardService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = {"board"})
public class BoardController {

    @Setter(onMethod_ = {@Autowired})
    private BoardService boardService;

    @GetMapping(path = {"/write"})
    public String write(Model model) {

        List<BoardLargeCategoryDto> largeCategories = boardService.findAllCategory();
        System.out.println(largeCategories);

        model.addAttribute("largeCategories", largeCategories);

        return "write";
    }

}
