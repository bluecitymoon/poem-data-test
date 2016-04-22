(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-log', {
            parent: 'entity',
            url: '/job-log?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobLogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-log/job-logs.html',
                    controller: 'JobLogController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('job-log-detail', {
            parent: 'entity',
            url: '/job-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobLog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-log/job-log-detail.html',
                    controller: 'JobLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobLog', function($stateParams, JobLog) {
                    return JobLog.get({id : $stateParams.id});
                }]
            }
        })
        .state('job-log.new', {
            parent: 'job-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-log/job-log-dialog.html',
                    controller: 'JobLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                result: null,
                                start: null,
                                end: null,
                                message: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-log', null, { reload: true });
                }, function() {
                    $state.go('job-log');
                });
            }]
        })
        .state('job-log.edit', {
            parent: 'job-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-log/job-log-dialog.html',
                    controller: 'JobLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobLog', function(JobLog) {
                            return JobLog.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-log', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-log.delete', {
            parent: 'job-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-log/job-log-delete-dialog.html',
                    controller: 'JobLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobLog', function(JobLog) {
                            return JobLog.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-log', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
