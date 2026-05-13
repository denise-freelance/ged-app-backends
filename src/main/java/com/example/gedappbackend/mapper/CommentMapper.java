package com.example.gedappbackend.mapper;

public interface CommentMapper {

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "authorAvatar", source = "author.avatarUrl")
    @Mapping(target = "replies", ignore = true)
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    @AfterMapping
    default void addReplies(@MappingTarget CommentResponse response, Comment comment) {
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            response.setReplies(toResponseList(comment.getReplies()));
        }
    }
}
