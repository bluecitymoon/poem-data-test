(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('JobLogDeleteController',JobLogDeleteController);

    JobLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobLog'];

    function JobLogDeleteController($uibModalInstance, entity, JobLog) {
        var vm = this;
        vm.jobLog = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            JobLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
