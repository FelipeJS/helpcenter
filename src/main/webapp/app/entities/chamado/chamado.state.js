(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chamado', {
            parent: 'entity',
            url: '/chamado',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'helpcenterApp.chamado.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chamado/chamados.html',
                    controller: 'ChamadoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chamado');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('empresa');
                    $translatePartialLoader.addPart('problema');
                    $translatePartialLoader.addPart('severidade');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('chamado-detail', {
            parent: 'entity',
            url: '/chamado/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'helpcenterApp.chamado.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chamado/chamado-detail.html',
                    controller: 'ChamadoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chamado');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('empresa');
                    $translatePartialLoader.addPart('problema');
                    $translatePartialLoader.addPart('severidade');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Chamado', function($stateParams, Chamado) {
                    return Chamado.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chamado',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chamado-detail.edit', {
            parent: 'chamado-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chamado/chamado-dialog.html',
                    controller: 'ChamadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chamado', function(Chamado) {
                            return Chamado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chamado.new', {
            parent: 'chamado',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chamado/chamado-dialog.html',
                    controller: 'ChamadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                empresa: null,
                                problema: null,
                                descricao: null,
                                severidade: null,
                                sugestao: null,
                                email: null,
                                solucao: null,
                                dataAberto: null,
                                dataFechado: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chamado', null, { reload: 'chamado' });
                }, function() {
                    $state.go('chamado');
                });
            }]
        })
        .state('chamado.edit', {
            parent: 'chamado',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chamado/chamado-dialog.html',
                    controller: 'ChamadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chamado', function(Chamado) {
                            return Chamado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chamado', null, { reload: 'chamado' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chamado.delete', {
            parent: 'chamado',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chamado/chamado-delete-dialog.html',
                    controller: 'ChamadoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Chamado', function(Chamado) {
                            return Chamado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chamado', null, { reload: 'chamado' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
