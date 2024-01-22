package org.learning.springblogricette.controller;


import jakarta.validation.Valid;
import org.learning.springblogricette.model.Category;
import org.learning.springblogricette.model.Recipe;
import org.learning.springblogricette.repository.CategoryRepository;
import org.learning.springblogricette.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @GetMapping
    public String index(Model model) {
        List<Recipe> recipeList = recipeRepository.findAll();
        model.addAttribute("recipeList", recipeList);
        return "recipes/list";
    }


    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe formRecipe = recipeOptional.get();
            model.addAttribute("formRecipe", formRecipe);
            return "recipes/detail";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/create")
    public String create(Model model) {
        Recipe recipe = new Recipe();
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("recipe", recipe);
        model.addAttribute("categoryList", categoryList);
        return "recipes/create";
    }


    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("recipe") Recipe formRecipe, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("Recipe", recipeRepository.findAll());
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "recipes/create";
        }
        recipeRepository.save(formRecipe);
        return "redirect:/recipes";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable Integer id, Model model) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            model.addAttribute("formRecipe", optionalRecipe.get());
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "recipes/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/edit/{id}")
    public String store(@PathVariable Integer id, Model model, @Valid @ModelAttribute("recipe") Recipe formRecipe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", categoryRepository.findAll());
            return "recipes/edit";
        }
        recipeRepository.save(formRecipe);
        return "redirect:/recipes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        recipeRepository.deleteById(id);
        return "redirect:/recipes";

    }


}
