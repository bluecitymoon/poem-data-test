(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('JobLogDetailController', JobLogDetailController);

    JobLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'JobLog', 'Job'];

    function JobLogDetailController($scope, $rootScope, $stateParams, entity, JobLog, Job) {
        var vm = this;
        vm.jobLog = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:jobLogUpdate', function(event, result) {
            vm.jobLog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
