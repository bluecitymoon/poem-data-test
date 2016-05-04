(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('StoryDialogController', StoryDialogController);

    StoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Story'];

    function StoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Story) {
        var vm = this;
        vm.story = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('poemdataApp:storyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.story.id !== null) {
                Story.update(vm.story, onSaveSuccess, onSaveError);
            } else {
                Story.save(vm.story, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
