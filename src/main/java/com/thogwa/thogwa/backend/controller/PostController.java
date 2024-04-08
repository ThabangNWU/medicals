package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.*;
import com.thogwa.thogwa.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping(name = "blog")
public class PostController {
    private final PostService postService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 8;
    private static final int[] PAGE_SIZES = {8, 12, 16, 18, 20};
    @Autowired
    public PostController(PostService postService){
        this.postService = postService;;
    }

    @PostMapping("/post")
    public ResponseEntity<Post> addPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("image") MultipartFile image,
                                        Principal principal
    ) {
        String email = principal.getName();
        postService.createPost(email,title,content,image);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/post/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Post getPostById(@PathVariable Integer id) {
        return postService.findPostByID(id);
    }
    @PostMapping("/image")
    public ResponseEntity<?> addPicture(@RequestParam("image") MultipartFile image) {
        postService.uploadImage(image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {
        Resource resource = postService.readFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }


    @GetMapping("/all")
    public ModelAndView showPosts(@RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer> page) {
        var modelAndView = new ModelAndView("blog");
        Page<Post> posts;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        posts = postService.findAllPageable(evalPage, evalPageSize);

        var pager = new Pager(posts.getTotalPages(), posts.getNumber(), BUTTONS_TO_SHOW);
        modelAndView.addObject("posts", posts);
        modelAndView.addObject("totalItems", posts.getTotalElements());
        modelAndView.addObject("totalPages", posts.getTotalPages());
        modelAndView.addObject("selectedPageSize", pageSize.orElse(INITIAL_PAGE_SIZE));
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);

        return modelAndView;
    }

    @GetMapping("/blog-details/{postId}")
    public String gePostDetails(@PathVariable Integer postId, Model model) {
        Post post = postService.findPostByID(postId);
        model.addAttribute("post",post);
        return "shop-details";
    }


    @PutMapping("/{postId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updatePost(
            Principal principal,
            @PathVariable Integer postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image) {
        String email = principal.getName();
        postService.updatePost(email,postId,title,content,image);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deletePost(@PathVariable Integer id,Principal principal) {
        String email = principal.getName();
        postService.deletePost(id,email);

        return ResponseEntity.noContent().build();
    }

}
