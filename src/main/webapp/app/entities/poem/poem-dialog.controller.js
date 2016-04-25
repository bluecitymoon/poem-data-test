(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemDialogController', PoemDialogController);

    PoemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Poem', 'Author'];

    function PoemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Poem, Author) {
        var vm = this;
        vm.poem = entity;
        vm.authors = Author.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:poemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.poem.id !== null) {
                Poem.update(vm.poem, onSaveSuccess, onSaveError);
            } else {
                Poem.save(vm.poem, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
