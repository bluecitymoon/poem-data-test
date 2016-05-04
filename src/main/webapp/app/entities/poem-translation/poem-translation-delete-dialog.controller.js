(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemTranslationDeleteController',PoemTranslationDeleteController);

    PoemTranslationDeleteController.$inject = ['$uibModalInstance', 'entity', 'PoemTranslation'];

    function PoemTranslationDeleteController($uibModalInstance, entity, PoemTranslation) {
        var vm = this;
        vm.poemTranslation = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PoemTranslation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
