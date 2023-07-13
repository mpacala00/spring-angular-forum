package com.github.mpacala00.forum.controller.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;
import com.github.mpacala00.forum.pojos.HttpResponse;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.data.CategoryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("categories")
public class CategoryController {

    CategoryService categoryService;
    UserAuthenticationService userAuthenticationService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryPostsDTO> getCategoryById(@PathVariable Long categoryId, @RequestHeader Map<String, String> headers) {
        Optional<User> userOpt = userAuthenticationService.retrieveByRequestHeadersToken(headers);

        CategoryPostsDTO categoryPostsDTO = categoryService.findByIdMapToDTO(categoryId,
                userOpt.map(User::getId).orElse(null));

        return new ResponseEntity<>(categoryPostsDTO, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('content:add')")
    public ResponseEntity<Category> publishCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/follow")
    public ResponseEntity<HttpResponse> followCategory(@PathVariable Long categoryId, @AuthenticationPrincipal User user) {
        categoryService.followCategory(categoryId, user, true);

        return HttpResponse.createResponseEntity(HttpStatus.OK, "Category followed");
    }

    @PutMapping("/{categoryId}/unfollow")
    public ResponseEntity<HttpResponse> unfollowCategory(@PathVariable Long categoryId, @AuthenticationPrincipal User user) {
        categoryService.followCategory(categoryId, user, false);

        return HttpResponse.createResponseEntity(HttpStatus.OK, "Category unfollowed");
    }

}
