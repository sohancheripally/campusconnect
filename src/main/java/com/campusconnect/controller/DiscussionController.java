
package com.campusconnect.controller;

import com.campusconnect.model.Post;
import com.campusconnect.model.User;
import com.campusconnect.repository.PostRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public DiscussionController(
            PostRepository postRepository,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping
    public Post createPost(@RequestBody Post request,
                           Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorName(user.getName());
        post.setAuthorRole(user.getRole().name());

        return postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id,
                           Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (user.getRole() != com.campusconnect.model.Role.ADMIN
                && !post.getAuthorName().equals(user.getName())) {
            throw new IllegalArgumentException(
                    "You can only delete your own posts");
        }

        postRepository.delete(post);
    }
}
