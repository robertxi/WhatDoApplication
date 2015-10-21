package com.robert.whatdo.controller;

import com.robert.whatdo.model.Comment;
import com.robert.whatdo.model.Task;
import com.robert.whatdo.model.TaskItem;
import com.robert.whatdo.service.CommentService;
import com.robert.whatdo.service.TaskItemService;
import com.robert.whatdo.service.TaskService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@RestController
public class HelloController {

    @RequestMapping(value="/remove", method=RequestMethod.POST)
    public Task remove(@RequestBody TaskItem data){
        TaskItemService.removeTaskItem(data);
        Task ret = TaskService.getTask(data.getTask_id());
        ret.getTaskList();
        ret.getTaskListSize();
        return ret;
    }

    @RequestMapping(value="/deleteTask", method=RequestMethod.POST)
    public void deleteTask(@RequestBody Task task){
        TaskService.deleteTask(task);
    }
    @RequestMapping(value="/init/")
    public List<Task> initialize(){
        List<Task> retList;
        //insert 1 as user ID for now, in future, will pass user object instead
        retList = TaskService.getTaskList(1);
        return retList;
    }
    @RequestMapping(value="/addTaskItem", method=RequestMethod.POST)
    public Task addTaskItem(@RequestBody JSONObject obj){

        TaskItem item = new TaskItem();
        item.setContent((String) obj.get("content"));
        item.setTask_id((int) obj.get("task_id"));
        item.setStatus((String) obj.get("status"));
        item.setDate_created((String) obj.get("date_created"));
        item.setDate_modified((String) obj.get("date_modified"));
        TaskItemService.addTaskItem(item);
        Task ret = TaskService.getTask(item.getTask_id());
        ret.setTaskList(TaskService.getTaskItems(ret));
        ret.setTaskListSize(ret.getTaskList().size());
        return ret;
    }
    @RequestMapping(value="/updateTaskItem",method=RequestMethod.POST)
    public Task updateTaskItem(@RequestBody JSONObject obj){
        TaskItem item = new TaskItem();

        item.setContent((String) obj.get("content"));
        item.setTask_id((int) obj.get("task_id"));
        item.setId((int) obj.get("id"));
        item.setStatus((String) obj.get("status"));
        item.setDate_modified((String) obj.get("date_modified"));
        TaskItemService.updateTaskItem(item);
        Task ret=TaskService.getTask(item.getTask_id());
        //again, 1 is just a temporary user ID
        ret.setTaskList(TaskService.getTaskItems(ret));
        ret.setTaskListSize(ret.getTaskList().size());
        return ret;
    }

    @RequestMapping(value="/addNewTask",method=RequestMethod.POST)
    public Task addNewTask(@RequestBody JSONObject obj){
        Task task = new Task();
        task.setTitle((String) obj.get("title"));
        task.setUser_id((int) obj.get("user_id"));
        task.setDate_created((String) obj.get("date_created"));
        task.setDate_modified((String) obj.get("date_modified"));
        task.setDescription((String) obj.get("description"));
        TaskService.addNewTask(task);
        Task retList;
        //insert 1 as user ID for now, in future, will pass user object instead
        retList=TaskService.getTaskByTitle(1, task.getTitle());
        return retList;
    }

    @RequestMapping(value="/addNewComment",method=RequestMethod.POST)
    public List<Comment> addNewComment(@RequestBody JSONObject obj){
        Comment comment = new Comment();
        comment.setTaskitem_id((int)obj.get("taskitem_id"));
        comment.setDate_created((String) obj.get("date_created"));
        comment.setUser_id((int) obj.get("user_id"));
        comment.setContent((String) obj.get("content"));
        List<Comment> retList = CommentService.addNewComment(comment);
        return retList;
    }
}
