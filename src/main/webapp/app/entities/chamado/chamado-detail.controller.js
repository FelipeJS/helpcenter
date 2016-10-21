(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoDetailController', ChamadoDetailController);

    ChamadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Chamado', 'User'];

    function ChamadoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Chamado, User) {
        var vm = this;

        vm.chamado = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('helpcenterApp:chamadoUpdate', function(event, result) {
            vm.chamado = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
