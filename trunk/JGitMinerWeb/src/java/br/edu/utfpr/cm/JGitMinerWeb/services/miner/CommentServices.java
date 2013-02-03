/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import java.util.Date;
import java.util.List;
import org.eclipse.egit.github.core.Comment;

/**
 *
 * @author Douglas
 */
public class CommentServices {

    public static EntityComment getCommentByIdComment(long idComment, GenericDao dao) {
        List<EntityComment> comments = dao.executeNamedQueryComParametros("Comment.findByIdComment", new String[]{"idComment"}, new Object[]{idComment});
        if (!comments.isEmpty()) {
            return comments.get(0);
        }
        return null;
    }

    public static EntityComment createEntity(Comment gitComment, GenericDao dao) {
        if (gitComment == null) {
            return null;
        }

        EntityComment comment = getCommentByIdComment(gitComment.getId(), dao);

        if (comment == null) {
            comment = new EntityComment();
        }

        comment.setMineredAt(new Date());
        comment.setCreatedAt(gitComment.getCreatedAt());
        comment.setUpdatedAt(gitComment.getUpdatedAt());
        comment.setBody(JsfUtil.filterChar(gitComment.getBody()));
        comment.setBodyHtml(gitComment.getBodyHtml());
        comment.setBodyText(gitComment.getBodyText());
        comment.setIdComment(gitComment.getId());
        comment.setUrl(gitComment.getUrl());
        comment.setUser(UserServices.createEntity(gitComment.getUser(), dao, false));

        if (comment.getId() == null || comment.getId().equals(new Long(0))) {
            dao.insert(comment);
        } else {
            dao.edit(comment);
        }

        return comment;
    }
}