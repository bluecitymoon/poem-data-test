(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemDeleteController',PoemDeleteController);

    PoemDeleteController.$inject = ['$uibModalInstance', 'entity', 'Poem'];

    function PoemDeleteController($uibModalInstance, entity, Poem) {
        var vm = this;
        vm.poem = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Poem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
