'use strict';

angular.module('birthreminderApp')
    .controller('PersonController', function ($scope, $state, Person, ParseLinks) {

        $scope.persons = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Person.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.persons = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.person = {
                name: null,
                lastName: null,
                birthDay: null,
                info: null,
                remind: null,
                id: null,
                owner: null,
                remindBefore: null,
                remindAfter: null
            };
        };
    });
