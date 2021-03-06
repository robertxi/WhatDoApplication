package com.robert.whatdo.service;

import com.robert.whatdo.model.TaskItem;

/**
 * Created by RobertXi on 10/6/15.
 */
public class TaskItemService {

    public static void removeTaskItem(int taskItem_id){
        WhatDoDAO.removeTaskItemComments(taskItem_id);
        WhatDoDAO.removeTaskItem(taskItem_id);
    }
    public static void addTaskItem(TaskItem item){
        WhatDoDAO.addTaskItem(item);
    }

    public static TaskItem updateTaskItem(TaskItem item){
        TaskItem ret = WhatDoDAO.updateTaskItem(item);
        return ret;
    }
    public static TaskItem getTaskItem(int id){
        TaskItem ret = WhatDoDAO.getTaskItemById(id);
        return ret;
    }

}
