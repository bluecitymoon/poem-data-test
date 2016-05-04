(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('StoryDeleteController',StoryDeleteController);

    StoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Story'];

    function StoryDeleteController($uibModalInstance, entity, Story) {
        var vm = this;
        vm.story = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Story.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
