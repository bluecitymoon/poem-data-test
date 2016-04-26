(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('ConfigurationDeleteController',ConfigurationDeleteController);

    ConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Configuration'];

    function ConfigurationDeleteController($uibModalInstance, entity, Configuration) {
        var vm = this;
        vm.configuration = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Configuration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
