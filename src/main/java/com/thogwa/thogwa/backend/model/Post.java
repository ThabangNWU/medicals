package com.thogwa.thogwa.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 9564)
    private String postTitle;
    @Column(length = 999999999)
    private String postContent;
    private String postImage;
    private LocalDate createAt;
    private String postBy;
}
