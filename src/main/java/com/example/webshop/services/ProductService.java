package com.example.webshop.services;

import com.example.webshop.models.Image;
import com.example.webshop.models.Product;
import com.example.webshop.models.User;
import com.example.webshop.repositories.ProductRepository;
import com.example.webshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;


/***
 * Product-objekt. Det initialiseras till en ny tom ArrayList,
 * vilket innebär att denna service initialt kommer att hantera
 * en tom lista av produkter. Denna lista fungerar som en
 * in-memory databas eller lager för att lagra produkter,
 * vilket är användbart i demo- eller utvecklingssyften
 * men skulle ersättas med en riktig databas i en produktionssituation.
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}


   /*

   -------------
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
-----------------
      public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }    }

     */


    /*
    private List<Product> products = new ArrayList<>();
    private long ID = 0;

    {
        products.add(new Product(++ID, "Playstation 5", "Simple description",
                5000, "Stockholm", "Dima"));

        products.add(new Product(++ID, "Iphone12","Simple description",
                10000, "Stockholm","Irina"));
    }

    public List<Product> listProducts(){
        return products;
    };

    public void saveProduct(Product product){
        product.setId(++ID);
        products.add(product);
    }
       public Product getProductById(Long id) {
        for (Product product : products) {
            if (product.getId().equals(id)) return product;
        }
        return null;
    }


     */