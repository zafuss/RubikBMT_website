package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.entities.Comment;
import zafus.rubikbmt.rubikbmt_website.repositories.IArticleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ICategoryRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ICommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IArticleRepository articleRepository;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment save(Comment comment, Article article) {
        Comment newComment = commentRepository.save(comment);
        if (comment.getParentComment() != null) {
            Comment parent = commentRepository.findById(newComment.getParentComment().getId()).orElseThrow();
            parent.getReplies().add(newComment);
            commentRepository.save(parent);
        } else {
            article.getComments().add(newComment);
        }
        articleRepository.save(article);
        return newComment;
    }

    public Comment findById(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}