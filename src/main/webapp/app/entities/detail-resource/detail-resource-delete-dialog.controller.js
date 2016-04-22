(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('DetailResourceDeleteController',DetailResourceDeleteController);

    DetailResourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'DetailResource'];

    function DetailResourceDeleteController($uibModalInstance, entity, DetailResource) {
        var vm = this;
        vm.detailResource = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            DetailResource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
