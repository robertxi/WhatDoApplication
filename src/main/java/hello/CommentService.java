package hello;

import java.util.List;

/**
 * Created by RobertXi on 10/11/15.
 */
public class CommentService {

    public static List<Comment> getCommentList(TaskItem item){
        List<Comment> ret = WhatDoDAO.getCommentList(item.getId());
        return ret;
    }

    public static List<Comment> addNewComment(Comment comment){
        WhatDoDAO.addNewComment(comment);
        List<Comment> ret = WhatDoDAO.getCommentList(comment.getTaskitem_id());
        return ret;
    }
}
