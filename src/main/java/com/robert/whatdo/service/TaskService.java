package com.robert.whatdo.service;

import com.robert.whatdo.model.Task;
import com.robert.whatdo.model.TaskItem;

import java.util.List;

/**
 * Created by RobertXi on 10/5/15.
 */
public class TaskService {


    public static void setId(Task task, int id){
        task.setId(id);
    }
    public static int getId(Task task){
        return task.getId();
    }
    public static List<TaskItem> getTaskItems(Task task){
        List<TaskItem> retList = WhatDoDAO.getTaskItems(task);
        for(TaskItem item: retList){
            item.setCommentList(WhatDoDAO.getCommentList(item.getId()));
        }
        return retList;
    }
    public static void deleteTask(Task task){
        WhatDoDAO.deleteTask(task);
    }
    public static List<Task> getTaskList(int userId){
        List<Task> retList = WhatDoDAO.getTaskList(userId);
        for(Task item : retList){
            item.setTaskListSize(item.getTaskList().size());
        }
        return retList;
    }
    public static void addNewTask(Task task){
        WhatDoDAO.addNewTask(task);
    }

    public static Task getTask(int task_id){
        Task ret = WhatDoDAO.getTaskById(task_id);
        List<TaskItem> itemList = TaskService.getTaskItems(ret);
        ret.setTaskList(itemList);
        ret.setTaskListSize(itemList.size());
        return ret;
    }
    public static Task getTaskByTitle(int user_id, String title){
        return WhatDoDAO.getTaskByTitle(user_id, title);
    }
}
