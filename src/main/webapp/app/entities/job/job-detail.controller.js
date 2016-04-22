(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Job', 'JobLog'];

    function JobDetailController($scope, $rootScope, $stateParams, entity, Job, JobLog) {
        var vm = this;
        vm.job = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
