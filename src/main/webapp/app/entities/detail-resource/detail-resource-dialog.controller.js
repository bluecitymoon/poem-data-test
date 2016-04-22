(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('DetailResourceDialogController', DetailResourceDialogController);

    DetailResourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DetailResource'];

    function DetailResourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DetailResource) {
        var vm = this;
        vm.detailResource = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:detailResourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.detailResource.id !== null) {
                DetailResource.update(vm.detailResource, onSaveSuccess, onSaveError);
            } else {
                DetailResource.save(vm.detailResource, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
