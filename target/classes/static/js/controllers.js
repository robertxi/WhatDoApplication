/**
 * Created by RobertXi on 9/29/15.
 */
var whatDoApp = angular.module('whatDoApp', ['ngRoute', 'ui.bootstrap', 'ngCookies']);

whatDoApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: 'whatDoTemplate4.html',
            controller: 'toDoListCtrl'
        }).when('/', {
            templateUrl: 'login.html',
            controller: 'loginCtrl'
        });
}]);

whatDoApp.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.authToken = $cookieStore.get('authToken'); //|| {};

        //not sure what this code does
        //if ($rootScope.globals.currentUser) {
        //    $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        //}

        $rootScope.$on('$locationChangeStart', function () {
            // redirect to login page if not logged in
            if ($location.path() !== '/' && $rootScope.authToken==null) {
                $location.path('/');
            }
        });
    }]);

whatDoApp.controller('loginCtrl', function ($scope, $http, $uibModal, $location, $rootScope, $cookieStore) {
    $scope.newuser = null;
    $rootScope.authToken = null;
    $scope.loginError = false;

    $scope.login = function () {
        var data = {'username': $scope.credentials.username, 'password': $scope.credentials.password};
        var res = $http.post("http://localhost:8080/loginAuth", data);
        res.success(function (data) {
            if (data != null) {
                $rootScope.authToken = data.token;
                $cookieStore.put("authToken", $rootScope.authToken);
                $location.path('/index');
            } else {
                alert("login information was incorrect");
            }
        });
        res.error(function () {
            alert("failure message: " + JSON.stringify({data: data}));
        })
    }

    $scope.register = function () {
        console.log("ready to open dialog");
        $uibModal.open({
            templateUrl: 'register.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope
        });
        console.log("dialog has been opened");
    };
});

whatDoApp.controller('updateDropDownCtrl', function ($scope, $log) {
    $scope.status = {isopen: false};
});

whatDoApp.controller('ModalInstanceCtrl', function ($scope, $http, $modalInstance, $rootScope) {

    $scope.register = function () {

        var username = $scope.newuser.username;
        var getuname = $http.post('http://localhost:8080/getUser', username);
        getuname.success(function (data) {
            if (data == true) {
                alert("That username is already taken");
            } else {
                var date = new Date().toDateString();
                var data = {
                    'username': $scope.newuser.username,
                    'password': $scope.newuser.password,
                    'fName': $scope.newuser.fname,
                    'lName': $scope.newuser.lname,
                    'email': $scope.newuser.email,
                    'date_created': date
                };

                var res = $http.post('http://localhost:8080/registerUser', data);
                res.success(function () {
                    $modalInstance.close();
                });
                res.error(function () {
                    alert("failure message: " + JSON.stringify({data: data}));
                    $modalInstance.close();
                });
            }
        });
        getuname.error = function () {
            alert("failure message: " + JSON.stringify({data: data}));
            $modalInstance.close();
        }

    };

    $scope.deleteTask = function (task, index) {
        var data ={
            'token':$rootScope.authToken,
            'task_id':task.id
        }
        var res = $http.post('http://localhost:8080/deleteTask', data);
        res.success(function () {
            console.log(task);
            $scope.list.splice($scope.currentDisplayTaskIndex, 1);
            $scope.$parent.currentDisplayTask = $scope.list[0];
            $modalInstance.close();
        });
        res.error(function () {
            alert("unable to delete task list");
        })
    };

    $scope.submitComment = function (item, task) {
        var date = new Date().toDateString();
        //set user_id to 1 until multiuser implemented
        var data = {'taskitem_id': item.id, 'content': $scope.newComment, 'date_created': date, 'user_id': 1};
        var res = $http.post('http://localhost:8080/addNewComment', data);
        res.success(function (data) {
            $scope.list[$scope.list.indexOf(task)].taskList[task.taskList.indexOf(item)].commentList = data;
            $modalInstance.close();
        });
        res.error(function (data) {
            alert("failure message: " + JSON.stringify({data: data}));
            $modalInstance.close();
        });
    };
    $scope.submitNewTask = function () {
        var date = new Date().toDateString();
        //temporarily add 1 as the user id until multi user system gets implemented
        var data = {
            'title': $scope.newTaskTitle,
            'token': $rootScope.authToken,
            'date_created': date,
            'date_modified': date,
            'description': $scope.newTaskDesc
        }
        console.log(data);
        var res = $http.post('http://localhost:8080/addNewTask', data);
        res.success(function (data, status, headers, config) {
            $scope.list.push(data);
            $modalInstance.close();
        });
        res.error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({data: data}));
            $modalInstance.close();
        });
    };

    $scope.submitModStatus = function (task) {
        var date = new Date().toDateString();
        var data = {
            'content': $scope.currentDisplayTaskItem.content,
            'id': $scope.currentDisplayTaskItem.id,
            'date_modified': date,
            'status': $scope.newTaskItemStatus,
            'task_id': $scope.currentDisplayTaskItem.task_id,
            'token':$rootScope.authToken
        };
        console.log(data);
        var res = $http.put('http://localhost:8080/updateTaskItem/', data);
        res.success(function (data, status, headers, config) {
            $scope.list[$scope.list.indexOf(task)] = data;
            $scope.currentDisplayTask.taskList = data.taskList;
            $modalInstance.close();
        });
        res.error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({data: data}));
        });
        $modalInstance.close();

    };
    $scope.submitModItem = function (task) {
        var date = new Date().toDateString();
        var data = {
            'content': $scope.newTaskItemContent,
            'id': $scope.currentDisplayTaskItem.id,
            'date_modified': date,
            'status': $scope.currentDisplayTaskItem.status,
            'task_id': $scope.currentDisplayTaskItem.task_id,
            'token':$rootScope.authToken
        };
        console.log(data);
        var res = $http.put('http://localhost:8080/updateTaskItem/', data);
        res.success(function (data, status, headers, config) {
            $scope.list[$scope.list.indexOf(task)] = data;
            $scope.currentDisplayTask.taskList = data.taskList;
            $modalInstance.close();
        });
        res.error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({data: data}));
        });
        $modalInstance.close();
    };

    $scope.submitAddNewItem = function (task) {
        var date = new Date().toDateString();
        var data = {
            'content': $scope.newTaskContent,
            'task_id': $scope.currentDisplayTask.id,
            'date_created': date,
            'date_modified': date,
            'status': "ongoing",
            'token': $rootScope.authToken
        };
        console.log(data);
        var res = $http.post('http://localhost:8080/addTaskItem/', data);
        res.success(function (data, status, headers, config) {
            if(data!=null) {
                console.log(data);
                $scope.list[$scope.list.indexOf(task)] = data;
                $scope.currentDisplayTask.taskList = data.taskList;
                $modalInstance.close();
            }else{
                alert("you need to log in first");
            }


        });
        res.error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({data: data}));
        });
    }
    $scope.cancelWindow = function () {
        $modalInstance.dismiss();
    }

    $scope.newTaskItemContent = "";
});

whatDoApp.controller('toDoListCtrl', function ($scope, $http, $uibModal, $rootScope, $cookieStore, $location) {
    $scope.list = [];
    $scope.newTaskContent = "";
    $scope.shown = true;
    $scope.openAccor = false;
    $scope.comment = {newComment: ''};
    $scope.currentDisplayTask = null;
    $scope.currentDisplayTaskIndex = null;
    $scope.animationsEnabled = true;
    $scope.currentDisplayTaskItem = null;

    $scope.logout = function () {
        $rootScope.authToken = null;
        $cookieStore.put("authToken", $rootScope.authToken);

        $location.path('/');
    }
    $scope.modTaskStatusModal = function (index) {
        $scope.currentDisplayTaskItem = $scope.currentDisplayTask.taskList[index];
        console.log("ready to open dialog");
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'modTaskStatusModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
        console.log("dialog has been opened");
    };
    $scope.modTaskItemModal = function (index) {
        $scope.currentDisplayTaskItem = $scope.currentDisplayTask.taskList[index];
        console.log("ready to open dialog");
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'modTaskItemModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
        console.log("dialog has been opened");
    };
    $scope.newTaskModal = function () {
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'newTaskModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
    };
    $scope.addTaskItemModal = function () {
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'addTaskItemModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
    };
    $scope.deleteTaskModal = function () {
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'removeTaskModal.html',
            controller: 'ModalInstanceCtrl',
            scope: $scope,
        });
    };
    $scope.submitComment = function (item, task) {
        var date = new Date().toDateString();
        //set user_id to 1 until multiuser implemented
        var data = {'taskitem_id': item.id, 'content': $scope.comment.newComment, 'date_created': date, 'token': $rootScope.authToken};
        var res = $http.post('http://localhost:8080/addNewComment', data);
        res.success(function (data) {
            //$scope.list[$scope.list.indexOf(task)].taskList[task.taskList.indexOf(item)].commentList=data;
            $scope.currentDisplayTask.taskList[$scope.currentDisplayTask.taskList.indexOf(item)].commentList = data;
            $scope.comment.newComment = "";
        });
        res.error(function (data) {
            alert("failure message: " + JSON.stringify({data: data}));
        });
    };

    $scope.removeTaskItem = function (index, taskItem, task) {
        var data = {
            'task_id': taskItem.task_id,
            'taskItem_id': taskItem.id,
            'token':$rootScope.authToken
        }

        var res = $http.post('http://localhost:8080/remove/', data);
        res.success(function (data, status, headers, config) {
            $scope.message = data;
            $scope.list[$scope.currentDisplayTaskIndex] = data;
            $scope.currentDisplayTask.taskList = data.taskList;
        });
        res.error(function (data, status, headers, config) {
            alert("failure message: " + JSON.stringify({data: data}));
        });

    };


    $scope.init = function () {

        var usrtoken = $rootScope.authToken;
        $http.get('http://localhost:8080/init/', {params: {'usrtoken': usrtoken}}).then(function (response) {
            $scope.list = response.data;
            $scope.currentDisplayTask = $scope.list[0];
            $scope.currentDisplayTaskIndex = 0;
        });
    };

    $scope.setDisplayTask = function (task) {
        $scope.currentDisplayTask = task;
        $scope.currentDisplayTaskIndex = $scope.list.indexOf(task);
    }

});