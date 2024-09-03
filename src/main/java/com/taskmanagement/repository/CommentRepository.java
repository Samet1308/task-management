package com.taskmanagement.repository;

import com.taskmanagement.dtos.CommentDto;
import com.taskmanagement.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Long> {
    List<Comment> findAllByTaskId(Long taskId);
}
