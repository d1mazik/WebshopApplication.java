package com.example.webshop.controllers;

import com.example.webshop.models.Product;
import com.example.webshop.models.User;
import com.example.webshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j // Lombok annotation för att skapa en logg
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }


    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/my/products";
    }



    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }

/*

---------
@PostMapping("/product/delete/{id}")
public String deleteProduct(@PathVariable Long id) {
    productService.deleteProductById(id); // Antag att denna metod endast kräver produktens ID
    return "redirect:/my/products";
}
---------------------------

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            log.info("CSRF Token: {}", csrfToken.getToken());
        } else {
            log.info("CSRF Token not found");
        }



        // Hämta User-objektet baserat på Principal
        User user = productService.getUserByPrincipal(principal);
        if (user != null) {
            log.info("Attempting to delete product with ID: {} by user ID: {}", id, user.getId());
            productService.deleteProduct(user, id); // Antag att denna metod tar ett User-objekt och produktens ID
            log.info("Product with ID: {} has been deleted successfully by user ID: {}", id, user.getId());
        } else {
            log.info("User not found for principal: {}", principal.getName());
        }

        return "redirect:/my/products";
    }

         */

}
