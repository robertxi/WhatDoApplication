/**
 * Created by RobertXi on 9/29/15.
 */
var whatDoApp = angular.module('whatDoApp',['ngRoute','ui.bootstrap']);

whatDoApp.config(['$routeProvider',function($routeProvider){
    $routeProvider.
        when('/',{
            templateUrl:'whatDoTemplate4.html',
            controller: 'toDoListCtrl'
        });
}]);
whatDoApp.controller('commentModalCtrl',function($scope,$http,$uibModal){
    $scope.animationsEnabled=true;
    $scope.commentModal=function(){
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'commentModal.html',
            controller:'ModalInstanceCtrl',
            scope:$scope,
        });
    };
});
whatDoApp.controller('newTaskModalCtrl',function($scope,$http,$uibModal){
   $scope.animationsEnabled=true;
    $scope.newTaskModal=function(){
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'newTaskModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
    };
});
whatDoApp.controller('updateDropDownCtrl', function ($scope, $log) {
    $scope.status = {isopen: false};
});

whatDoApp.controller('commentModalCtrl',function($scope,$http,$uibModal){
    console.log("in the redirecting controller");
    $scope.animationsEnabled=true;
    $scope.initCommentModal=function(){
        console.log("ready to open dialog");
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'commentModal.html',
            controller: 'ModalInstanceCtrl',
            scope:$scope,
        });
        console.log("dialog has been opened");
    };
});
whatDoApp.controller('modTaskItemModalCtrl',function($scope,$http,$uibModal){
    console.log("in the redirecting controller");
    $scope.animationsEnabled=true;
    $scope.modTaskItemModal=function(){
        console.log("ready to open dialog");
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'modTaskItemModal.html',
            controller: 'ModalInstanceCtrl',
            scope:$scope,
        });
        console.log("dialog has been opened");
    };
});
whatDoApp.controller('modTaskStatusModalCtrl',function($scope,$http,$uibModal){
    console.log("in the redirecting controller");
    $scope.animationsEnabled=true;
    $scope.modTaskStatusModal=function(){
        console.log("ready to open dialog");
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'modTaskStatusModal.html',
            controller: 'ModalInstanceCtrl',
            scope:$scope,
        });
        console.log("dialog has been opened");
    };
});
whatDoApp.controller('addTaskModalCtrl',function($scope,$http,$uibModal){
    $scope.animationsEnabled=true;
    $scope.addTaskItemModal=function(){
        $uibModal.open({
            animation:$scope.animationsEnabled,
            templateUrl: 'addTaskItemModal.html',
            controller: 'ModalInstanceCtrl',
            scope:$scope,
        });
   };
});

whatDoApp.controller('ModalInstanceCtrl',function($scope,$http,$modalInstance){

    $scope.submitComment=function(item,task){
        var date = new Date().toDateString();
        //set user_id to 1 until multiuser implemented
        var data = {'taskitem_id':item.id,'content':$scope.newComment,'date_created':date, 'user_id':1 };
        var res=$http.post('http://localhost:8080/addNewComment',data);
        res.success(function(data){
            $scope.list[$scope.list.indexOf(task)].taskList[task.taskList.indexOf(item)].commentList=data;
            $modalInstance.close();
        });
        res.error(function(data){
            alert("failure message: "+JSON.string({data:data}));
            $modalInstance.close();
        });
    };
    $scope.submitNewTask=function(){
        var date = new Date().toDateString();
        //temporarily add 1 as the user id until multi user system gets implemented
        var data = {'title':$scope.newTaskTitle,'user_id': 1, 'date_created': date, 'date_modified':date,'description':$scope.newTaskDesc}
        console.log(data);
        var res = $http.post('http://localhost:8080/addNewTask',data);
        res.success(function(data,status,headers,config){
            $scope.list.push(data);
            $modalInstance.close();
        });
        res.error(function(data,status,headers,config){
            alert("failure message: " +JSON.stringify({data:data}));
            $modalInstance.close();
        });
    };

    $scope.submitModStatus=function(task){
        var date = new Date().toDateString();
        var data = {'content':$scope.item.content,'id':$scope.item.id,'date_modified':date,'status':$scope.newTaskItemStatus,'task_id':$scope.item.task_id};
        console.log(data);
        var res = $http.post('http://localhost:8080/updateTaskItem/', data);
        res.success(function(data,status,headers,config){
            $scope.list[$scope.list.indexOf(task)] = data;
            $scope.currentDisplayTask.taskList=data.taskList;
            $modalInstance.close();
        });
        res.error(function(data,status,headers,config){
            alert("failure message: " + JSON.stringify({data:data}));
        });
        $modalInstance.close();

    };
    $scope.submitModItem=function(task){
        var date = new Date().toDateString();
        var data = {'content':$scope.newTaskItemContent,'id':$scope.item.id,'date_modified':date,'status':$scope.item.status,'task_id':$scope.item.task_id};
        console.log(data);
        var res = $http.post('http://localhost:8080/updateTaskItem/', data);
        res.success(function(data,status,headers,config){
            $scope.list[$scope.list.indexOf(task)] = data;
            $scope.currentDisplayTask.taskList = data.taskList;
            $modalInstance.close();
        });
        res.error(function(data,status,headers,config){
            alert("failure message: " + JSON.stringify({data:data}));
        });
        $modalInstance.close();
    };

    $scope.submitAddNewItem=function(task){
        var date = new Date().toDateString();
        var data = {'content': $scope.newTaskContent,'task_id':$scope.currentDisplayTask.id,'date_created':date,'date_modified':date,'status':"ongoing"};
        console.log(data);
        var res = $http.post('http://localhost:8080/addTaskItem/', data);
        res.success(function(data,status,headers,config){
            console.log(data);
            $scope.list[$scope.list.indexOf(task)]=data;
            $scope.currentDisplayTask.taskList = data.taskList;
            $modalInstance.close();

        });
        res.error(function(data,status,headers,config){
            alert("failure message: " + JSON.stringify({data:data}));
        });
    }
    $scope.cancelWindow=function(){
        $modalInstance.dismiss();
    }

    $scope.newTaskItemContent="";
});

whatDoApp.controller('toDoListCtrl',function($scope, $http){
    $scope.list=[];
    $scope.newTaskContent="";
    $scope.shown = true;
    $scope.openAccor=false;
    $scope.comment={newComment:''};
    $scope.currentDisplayTask=null;
    $scope.currentDisplayTaskIndex=null;

    $scope.submitComment=function(item,task){
        var date = new Date().toDateString();
        //set user_id to 1 until multiuser implemented
        var data = {'taskitem_id':item.id,'content':$scope.comment.newComment,'date_created':date, 'user_id':1 };
        var res=$http.post('http://localhost:8080/addNewComment',data);
        res.success(function(data){
            //$scope.list[$scope.list.indexOf(task)].taskList[task.taskList.indexOf(item)].commentList=data;
            $scope.currentDisplayTask.taskList[$scope.currentDisplayTask.taskList.indexOf(item)].commentList=data;
            $scope.comment.newComment="";
        });
        res.error(function(data){
            alert("failure message: "+JSON.string({data:data}));
        });
    };

    $scope.removeTaskItem=function(index, taskItem, task){

        var res = $http.post('http://localhost:8080/remove/', taskItem);
        res.success(function(data,status,headers,config){
            $scope.message=data;
            $scope.list[$scope.currentDisplayTaskIndex]=data;
            $scope.currentDisplayTask.taskList = data.taskList;
        });
        res.error(function(data,status,headers,config){
            alert("failure message: " + JSON.stringify({data:data}));
        });

    };

    $scope.deleteTask=function(task, index){
        var res = $http.post('http://localhost:8080/deleteTask', task)
        res.success(function(){
            $scope.list.splice($scope.currentDisplayTaskIndex,1);
        });
        res.error(function(){
            alert("unable to delete task list");
        })
    };

    $scope.init = function(){
        $http.get('http://localhost:8080/init/').then(function(response){
            $scope.list = response.data;
            $scope.currentDisplayTask=$scope.list[0];
            $scope.currentDisplayTaskIndex = 0;
        });
    };

    $scope.setDisplayTask=function(task){
        $scope.currentDisplayTask = task;
        $scope.currentDisplayTaskIndex=$scope.list.indexOf(task);
    }

});