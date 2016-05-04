(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('poem-translation', {
            parent: 'entity',
            url: '/poem-translation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PoemTranslations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/poem-translation/poem-translations.html',
                    controller: 'PoemTranslationController',
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
        .state('poem-translation-detail', {
            parent: 'entity',
            url: '/poem-translation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PoemTranslation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/poem-translation/poem-translation-detail.html',
                    controller: 'PoemTranslationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PoemTranslation', function($stateParams, PoemTranslation) {
                    return PoemTranslation.get({id : $stateParams.id});
                }]
            }
        })
        .state('poem-translation.new', {
            parent: 'poem-translation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem-translation/poem-translation-dialog.html',
                    controller: 'PoemTranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                translation: null,
                                poemId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('poem-translation', null, { reload: true });
                }, function() {
                    $state.go('poem-translation');
                });
            }]
        })
        .state('poem-translation.edit', {
            parent: 'poem-translation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem-translation/poem-translation-dialog.html',
                    controller: 'PoemTranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PoemTranslation', function(PoemTranslation) {
                            return PoemTranslation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('poem-translation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('poem-translation.delete', {
            parent: 'poem-translation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem-translation/poem-translation-delete-dialog.html',
                    controller: 'PoemTranslationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PoemTranslation', function(PoemTranslation) {
                            return PoemTranslation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('poem-translation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
