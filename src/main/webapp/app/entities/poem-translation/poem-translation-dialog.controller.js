(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemTranslationDialogController', PoemTranslationDialogController);

    PoemTranslationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PoemTranslation'];

    function PoemTranslationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PoemTranslation) {
        var vm = this;
        vm.poemTranslation = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:poemTranslationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.poemTranslation.id !== null) {
                PoemTranslation.update(vm.poemTranslation, onSaveSuccess, onSaveError);
            } else {
                PoemTranslation.save(vm.poemTranslation, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
