package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Post;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import com.thogwa.thogwa.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final StorageService storageService;
    @Autowired
    private CustomerRepository userRepository;

    @Autowired
    public PostService (PostRepository postRepository,
                        StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
    }
    public Post createPost(String email, String title, String content, MultipartFile file) {
        //set post title and post content
        Post post1 = new Post();
        post1.setPostTitle(title.trim());
        post1.setPostContent(content.trim());
        User user1 = userRepository.findByUsername(email);

        //set image if not empty
        String fileName = storageService.storeFile(file);
        if(!fileName.isEmpty()) {
            post1.setPostImage(fileName);
        }

        //set user if user is not null
        User user = userRepository.findById(user1.getId()).get();
//        if(user != null)
//            post1.setUser(user);
        //set date
//        Date now = new Date();
//        post1.setCreateAt(now) ;
//        post1.setUpdateAt(now);

        return postRepository.save(post1);
    }
    public Page<Post> findAllPageable(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return postRepository.findAll(pageable);
    }
    public void updatePost(String email, Integer postId, String title,
                           String content, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException(
                        "post with id [%s] not found".formatted(postId)));
        User user1 = userRepository.findByUsername(email);

        userRepository.findById(user1.getId())
                .filter(users -> users.getId() == user1.getId())
                .orElseThrow(() -> new RuntimeException(
                        "user with the id [%s] is not the own of this post".formatted(user1.getId())
                ));

        boolean change = false;

        if(!title.equals(post.getPostTitle()) && title != null) {
            post.setPostTitle(title);
            change = true;
        }

        if(content != null && !content.equals(post.getPostContent())) {
            post.setPostContent(content);
            change = true;
        }

        if(!change) {
            throw new RuntimeException("no data changes");
        }

        String oldImageName = post.getPostImage();
        if(!image.isEmpty()) {
            post.setPostImage(uploadImage(image));
            if(oldImageName != null && !oldImageName.equals("")){
                deleteImage(oldImageName);
            }
        }

        postRepository.save(post);
    }
    public List<Post> posts(){
        return postRepository.findAll();
    }
    public Post findPostByID(Integer id) {
        return postRepository.findById(id).get();
    }
    public Resource readFile(String fileName) {
        Resource resource = storageService.loadAsResource(fileName);
        return resource;
    }
    public String uploadImage(MultipartFile image) {
        return storageService.storeFile(image);
    }
    private void deleteImage(String imageName) {
        storageService.delete(imageName);
    }

    public void deletePost(Integer postId,String email) {
        User user1 = userRepository.findByUsername(email);

        userRepository.findById(user1.getId())
                .filter(users -> users.getId() == user1.getId())
                .orElseThrow(() -> new RuntimeException(
                        "user with the id [%s] is not the own of this post".formatted(user1.getId())
                ));
        Post post = postRepository.findById(postId).orElseThrow(() -> new
                RuntimeException("Post with id [%s] doesn't exist".formatted(postId)));

        if(post.getPostImage() != null && !post.getPostImage().equals("")) {
            deleteImage(post.getPostImage());
        }
        postRepository.delete(post);
    }
}

