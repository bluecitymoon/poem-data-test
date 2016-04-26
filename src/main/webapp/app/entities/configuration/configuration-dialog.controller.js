(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('ConfigurationDialogController', ConfigurationDialogController);

    ConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Configuration'];

    function ConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Configuration) {
        var vm = this;
        vm.configuration = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:configurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.configuration.id !== null) {
                Configuration.update(vm.configuration, onSaveSuccess, onSaveError);
            } else {
                Configuration.save(vm.configuration, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
