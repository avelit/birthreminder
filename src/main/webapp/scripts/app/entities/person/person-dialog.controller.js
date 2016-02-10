'use strict';

angular.module('birthreminderApp').controller('PersonDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Person',
        function($scope, $stateParams, $uibModalInstance, entity, Person) {

        $scope.person = entity;
        $scope.load = function(id) {
            Person.get({id : id}, function(result) {
                $scope.person = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('birthreminderApp:personUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.person.id != null) {
                Person.update($scope.person, onSaveSuccess, onSaveError);
            } else {
                Person.save($scope.person, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthDay = {};

        $scope.datePickerForBirthDay.status = {
            opened: false
        };

        $scope.datePickerForBirthDayOpen = function($event) {
            $scope.datePickerForBirthDay.status.opened = true;
        };
}]);
