package com.robert.whatdo.service;

import com.robert.whatdo.model.Comment;
import com.robert.whatdo.model.Task;
import com.robert.whatdo.model.TaskItem;
import com.robert.whatdo.model.User;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.json.simple.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.List;
import java.util.Random;

/**
 * Created by RobertXi on 10/5/15.
 */
public class WhatDoDAO {

    static String url = "jdbc:mysql://localhost:3306/";
    static String dbName = "whatdodb";
    static String driver = "com.mysql.jdbc.Driver";
    static String dbUser = "wdadmin";
    static String password = "wdpwd";
    static Connection conn = null;

    public static void main(String[] args) {
    }

    //queries
    private static final String GET_COMMENT_LIST = "SELECT * FROM comments WHERE taskitem_id = ?";
    private static final String GET_TASK_BY_TITLE = "SELECT * FROM task WHERE user_id=? AND title=?";
    private static final String GET_TASK = "SELECT * FROM task WHERE id=?";
    private static final String GET_TASK_LIST = "SELECT * FROM task WHERE user_id = ?";
    private static final String SELECT_TASKS = "Select * FROM task_item WHERE task_id =?";
    private static final String SELECT_TASKITEM_BY_ID = "SELECT * FROM task_item WHERE id=?";
    private static final String CHECK_USERNAME="SELECT * FROM users WHERE username=?";
    private static final String LOGIN="SELECT * FROM users WHERE username=? AND password=?";
    private static final String CHECKAUTH="SELECT * FROM users WHERE token=?";

    //updates
    private static final String REGISTER_USER = "INSERT INTO users (username, password, fName, lName, email, date_created, token) VALUES(?,?,?,?,?,?,?)";
    private static final String INSERT_NEW_COMMENT = "INSERT INTO comments(taskitem_id,content,date_created,user_id) VALUES (?,?,?,?)";
    private static final String INSERT_TASK_ITEM = "INSERT INTO task_item (task_id,content,date_created,date_modified,status) VALUES (?,?,?,?,?)";
    private static final String ADD_NEW_TASK = "INSERT INTO task (user_id, title, description, date_created, date_modified) VALUES (?,?,?,?,?)";
    private static final String UPDATE_TASK_ITEM = "UPDATE task_item SET content=?, status=?, date_modified=? WHERE id=? ";
    private static final String REMOVE_TASK_ITEM_COMMENT = "DELETE FROM comments WHERE taskitem_id=?";
    private static final String REMOVE_TASK_ITEM = "DELETE FROM task_item WHERE id=?";
    private static final String DELETE_TASK = "DELETE FROM task WHERE id = ?";
    private static final String DELETE_TASK_ITEMS = "DELETE FROM task_item WHERE task_id = ?";

    private static Object[] queryParams = null;

    //######################
    //QUERIES
    //######################
    public static int ifAuth(String token){
        queryParams=new Object[]{token};
        User user = ifAuthImpl(CHECKAUTH, queryParams);
        if(null==user){
            return -1;
        }else{
            return user.getId();
        }
    }

    private static User ifAuthImpl(String query, Object[] params){
        User ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<User> rH = new BeanHandler<>(User.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static String loginAuth(JSONObject obj){
        queryParams=new Object[]{(String)obj.get("username"), (String)obj.get("password")};
        User user=loginAuthImpl(LOGIN,queryParams);
        if(null==user){
            return null;
        }else{
            return user.getToken();
        }
    }

    private static User loginAuthImpl(String query, Object[] params){
        User ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<User> rH = new BeanHandler<>(User.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }
    public static boolean checkUsername(String username){
        queryParams=new Object[]{username};
        User user = checkUsernameImpl(CHECK_USERNAME,queryParams);
        if(null==user){
            return false;
        }else{
            return true;
        }
    }

    private static User checkUsernameImpl(String query, Object[] params){
        User ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<User> rH = new BeanHandler<>(User.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static List<Comment> getCommentList(int id) {
        queryParams = new Object[]{id};
        //String getCommentListFinal=String.format(GET_COMMENT_LIST,id);
        List<Comment> retList = getCommentListImpl(GET_COMMENT_LIST, queryParams);
        return retList;
    }

    private static List<Comment> getCommentListImpl(String query, Object[] params) {
        List<Comment> ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<Comment>> rH = new BeanListHandler<>(Comment.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static Task getTaskByTitle(int user_id, String title) {
        queryParams = new Object[]{user_id, title};
        String getTaskByTitleFinal = String.format(GET_TASK_BY_TITLE, user_id, title);
        return getTaskImpl(GET_TASK_BY_TITLE, queryParams);
    }

    public static Task getTaskById(int task_id) {
        queryParams = new Object[]{task_id};
        String getTaskFinal = String.format(GET_TASK, task_id);
        Task ret = getTaskImpl(GET_TASK, queryParams);
        return ret;
    }

    private static Task getTaskImpl(String query, Object[] params) {
        Task ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<Task> rH = new BeanHandler<Task>(Task.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static TaskItem getTaskItemById(int id) {
        queryParams = new Object[]{id};
        //String queryFinal = String.format(SELECT_TASKITEM_BY_ID, id);
        TaskItem ret = getTaskItemByIdImpl(SELECT_TASKITEM_BY_ID, queryParams);
        return ret;
    }

    private static TaskItem getTaskItemByIdImpl(String query, Object[] params) {
        TaskItem ret = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<TaskItem> rH = new BeanHandler<TaskItem>(TaskItem.class);
            ret = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static List<TaskItem> getTaskItems(int task_id) {
        queryParams = new Object[]{task_id};
        //String selectTasksFinal = String.format(SELECT_TASKS, TaskService.getId(task));
        List<TaskItem> retList = getTaskItemSet(SELECT_TASKS, queryParams);
        return retList;
    }

    private static List<TaskItem> getTaskItemSet(String query, Object[] params) {
        List<TaskItem> list = null;
        try {
            DataSource datasource = MyDataSourceFactory.INSTANCE.getDataSource();
            //create new queryRunner and pass in datasource as argument
            QueryRunner run = new QueryRunner(datasource);
            ResultSetHandler<List<TaskItem>> rH = new BeanListHandler<TaskItem>(TaskItem.class);
            //execute query without conn, this is when you pass in datasource ito queryrunner
            list = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<Task> getTaskList(int user_id) {
        queryParams = new Object[]{user_id};

        List<Task> retList = getTaskListImpl(GET_TASK_LIST, queryParams);
        if(null!=retList) {
            for (Task task : retList) {
                task.setTaskList(TaskService.getTaskItems(task.getId()));
            }
        }
        return retList;
    }

    private static List<Task> getTaskListImpl(String query, Object[] params) {
        List<Task> retList = null;
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            ResultSetHandler<List<Task>> rH = new BeanListHandler<Task>(Task.class);
            retList = run.query(query, rH, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retList;
    }




    //######################
    //UPDATES
    //######################
    private static void executeUpdate(String query, Object[] params) {
        try {
            DataSource dataSource = MyDataSourceFactory.INSTANCE.getDataSource();
            QueryRunner run = new QueryRunner(dataSource);
            run.update(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    public static void registerUser(User user){
        Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        String token = DigestUtils.md5Hex(user.getUsername()+user.getPassword()+salt);
        Object[] params = new Object[]{user.getUsername(), user.getPassword(), user.getfName(), user.getlName(), user.getEmail(), user.getDate_created(), token};
        executeUpdate(REGISTER_USER,params);
    }

    public static void addNewComment(Comment comment) {
        Object[] params = new Object[]{comment.getTaskitem_id(), comment.getContent(), comment.getDate_created(), comment.getUser_id()};
        //String insertNewCommentFinal = String.format(INSERT_NEW_COMMENT,comment.getTaskitem_id(),comment.getContent(),comment.getDate_created(),comment.getUser_id());
        executeUpdate(INSERT_NEW_COMMENT, params);
    }

    public static void addNewTask(Task task) {
        Object[] params = new Object[]{task.getUser_id(), task.getTitle(), task.getDescription(), task.getDate_created(), task.getDate_modified()};
        //String addNewTaskFinal=String.format(ADD_NEW_TASK,task.getUser_id(),task.getTitle(),task.getDescription(),task.getDate_created(),task.getDate_modified());
        executeUpdate(ADD_NEW_TASK, params);
    }

    public static TaskItem updateTaskItem(TaskItem item) {
        Object[] params = new Object[]{item.getContent(), item.getStatus(), item.getDate_modified(), item.getId()};
        //String updateTaskItemFinal=String.format(UPDATE_TASK_ITEM,item.getContent(),item.getStatus(),item.getDate_modified(),item.getId());
        executeUpdate(UPDATE_TASK_ITEM, params);
        TaskItem ret = TaskItemService.getTaskItem(item.getId());
        return ret;
    }

    public static void addTaskItem(TaskItem item) {
        Object[] params = new Object[]{item.getTask_id(), item.getContent(), item.getDate_created(), item.getDate_modified(), item.getStatus()};
        //String insertTaskItemFinal = String.format(INSERT_TASK_ITEM,item.getTask_id(),item.getContent(),item.getDate_created(),item.getDate_modified(),item.getStatus());
        executeUpdate(INSERT_TASK_ITEM, params);
    }

    public static void deleteTask(int task_id) {
        Object[] paramsTask = new Object[]{task_id};
        Object[] paramsItems = new Object[]{task_id};
//        String deleteTaskFinal = String.format(DELETE_TASK, id);
//        String deleteTaskItemsFinal = String.format(DELETE_TASK_ITEMS,id);
        List<TaskItem> itemList = TaskService.getTaskItems(task_id);
        for (TaskItem item : itemList) {
            Object[] params = new Object[]{item.getId()};
            //String removeTaskItemCommentFinal = String.format(REMOVE_TASK_ITEM_COMMENT,item.getId());
            executeUpdate(REMOVE_TASK_ITEM_COMMENT, params);
        }
        executeUpdate(DELETE_TASK_ITEMS, paramsItems);
        executeUpdate(DELETE_TASK, paramsTask);

    }

    public static void removeTaskItemComments(int taskItem_id) {
        Object[] params = new Object[]{taskItem_id};
        //String removeTaskItemCommentFinal=String.format(REMOVE_TASK_ITEM_COMMENT, item.getId());
        executeUpdate(REMOVE_TASK_ITEM_COMMENT, params);
    }

    public static void removeTaskItem(int taskItem_id) {
        Object[] params = new Object[]{taskItem_id};
        //String removeTaskItemFinal = String.format(REMOVE_TASK_ITEM,taskItemID);
        executeUpdate(REMOVE_TASK_ITEM, params);
    }


}
