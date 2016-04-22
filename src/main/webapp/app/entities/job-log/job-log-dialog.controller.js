(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('JobLogDialogController', JobLogDialogController);

    JobLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobLog', 'Job'];

    function JobLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobLog, Job) {
        var vm = this;
        vm.jobLog = entity;
        vm.jobs = Job.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:jobLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.jobLog.id !== null) {
                JobLog.update(vm.jobLog, onSaveSuccess, onSaveError);
            } else {
                JobLog.save(vm.jobLog, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.end = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
