package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.*;
import com.geekbrains.springbootproject.repositories.specifications.ProductSpecs;
import com.geekbrains.springbootproject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private ProductsService productsService;
    private OrderService orderService;
    private CommentsService commentsService;
    private UserServiceImpl userService;
    private CategoriesService categoriesService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    @Autowired
    public void setCommentsService(CommentsService commentsService) {
        this.commentsService = commentsService;
    }
    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "catParam") Optional<Long> categoryId,
                           @RequestParam Map<String, String> paramsMap
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Long currentCategory = categoryId.orElse(0L);
        Category catObj = categoriesService.findById(currentCategory);

        ArrayList<Property> properties = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        Specification<Product> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();

     /*   if (word != null) {
            spec = spec.and(ProductSpecs.titleContains(word));
            filters.append("&word=" + word);
        }
        if (min != null) {
            spec = spec.and(ProductSpecs.priceGreaterThanOrEq(min));
            filters.append("&min=" + min);
        }
        if (max != null) {
            spec = spec.and(ProductSpecs.priceLesserThanOrEq(max));
            filters.append("&max=" + max);
        }*/
        if (catObj != null) {
            spec = spec.and(ProductSpecs.categoryEquals(catObj));
            categories = (ArrayList<Category>) categoriesService.getChildren(catObj);
            categories.add(0,catObj);
            properties = (ArrayList<Property>)catObj.getAllProperties(properties);
            String param;
            String param2;
            for (Property propCheck : properties){
                if (propCheck.getElement().getName().equals("value")){
                    String parName = propCheck.getName()+"first";
                    param = paramsMap.get(parName);
                    if (param != null) {
                    if (propCheck.getType().equals("text")){
                        spec = spec.and(ProductSpecs.createSpec("like",propCheck.getName(),param));
                        propCheck.setValueFirst(param);
                    }else{
                        spec = spec.and(ProductSpecs.createSpec("eq",propCheck.getName(),param));
                        model.addAttribute(parName,param);
                    }}
                }else if (propCheck.getElement().getName().equals("between")){
                    String parName = propCheck.getName()+"first";
                    String parName2 = propCheck.getName()+"second";
                    param = paramsMap.get(parName);
                    param2 = paramsMap.get(parName2);
                    if (param != null) {
                        spec = spec.and(ProductSpecs.createSpec("GTOE",propCheck.getName(),param));
                        propCheck.setValueFirst(param);
                    }
                    if (param2 != null) {
                        spec = spec.and(ProductSpecs.createSpec("LTOE",propCheck.getName(),param2));
                        propCheck.setValueSecond(param2);
                    }
                }
            }
            model.addAttribute("properties",properties);
        }else{
            categories = (ArrayList<Category>) categoriesService.getRootCategories();
            catObj = new Category();
            catObj.setId(0L);
        }

        Page<Product> products = productsService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        model.addAttribute("filters", filters.toString());


        model.addAttribute("categories", categories);
        model.addAttribute("category", catObj);
        return "shop-page";
    }

    @GetMapping("/{id}")
    public String showDetails(Model model, @PathVariable("id") Long id){
        Product CurrProduct = productsService.findById(id);
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currUser = userService.findByUserName(ud.getUsername());

        Comment newComment = new Comment();
        newComment.setProduct(CurrProduct);
        newComment.setUser(currUser);

        Page<Comment> comments = commentsService.findAllByProduct(CurrProduct,0);

        boolean allowed = orderService.havebeenOrdered(CurrProduct,currUser) && (commentsService.findByProductAndUser(CurrProduct,currUser) == null);

        model.addAttribute("comment", newComment);
        model.addAttribute("allowed", allowed);
        model.addAttribute("comments", comments.getContent());

        return "product-details";

    }

    @PostMapping("/postComm")
    public String addComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult bindingResult, Model model) {
        Long id = comment.getProduct().getId();
        commentsService.saveOrUpdate(comment);
        return "redirect:/shop/"+id.toString();
    }

    @PostMapping("/filter")
    public String setFilter(Model model,@RequestParam("catParam") Optional<Integer> catParam,@RequestParam Map<String, String> paramsMap){
        Integer categoryNumb = catParam.orElse(0);
        return "redirect:/shop?page=0"+"&catParam="+ categoryNumb.toString() + buildParams(paramsMap);
    }

    private String buildParams(Map<String, String> paramsMap){
        StringBuilder paramsString = new StringBuilder();

        if (!paramsMap.isEmpty()){
            String key;
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                key = entry.getKey();
                if (!entry.getValue().equals("") && (key.endsWith("first") || key.endsWith("second"))){
                    paramsString.append("&" + entry.getKey() + "="+entry.getValue());
                }
            }
        }
        return paramsString.toString();
    }

}
