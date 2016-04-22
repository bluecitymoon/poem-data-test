(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('detail-resource', {
            parent: 'entity',
            url: '/detail-resource?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DetailResources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detail-resource/detail-resources.html',
                    controller: 'DetailResourceController',
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
        .state('detail-resource-detail', {
            parent: 'entity',
            url: '/detail-resource/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DetailResource'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/detail-resource/detail-resource-detail.html',
                    controller: 'DetailResourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DetailResource', function($stateParams, DetailResource) {
                    return DetailResource.get({id : $stateParams.id});
                }]
            }
        })
        .state('detail-resource.new', {
            parent: 'detail-resource',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-resource/detail-resource-dialog.html',
                    controller: 'DetailResourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                outsideId: null,
                                visitCount: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('detail-resource', null, { reload: true });
                }, function() {
                    $state.go('detail-resource');
                });
            }]
        })
        .state('detail-resource.edit', {
            parent: 'detail-resource',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-resource/detail-resource-dialog.html',
                    controller: 'DetailResourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DetailResource', function(DetailResource) {
                            return DetailResource.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('detail-resource', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('detail-resource.delete', {
            parent: 'detail-resource',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/detail-resource/detail-resource-delete-dialog.html',
                    controller: 'DetailResourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DetailResource', function(DetailResource) {
                            return DetailResource.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('detail-resource', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
