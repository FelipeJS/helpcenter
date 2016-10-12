(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoDetailController', ChamadoDetailController);

    ChamadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chamado', 'User'];

    function ChamadoDetailController($scope, $rootScope, $stateParams, previousState, entity, Chamado, User) {
        var vm = this;

        vm.chamado = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('helpcenterApp:chamadoUpdate', function(event, result) {
            vm.chamado = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
