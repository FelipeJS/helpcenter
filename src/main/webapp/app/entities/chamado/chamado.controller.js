(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoController', ChamadoController);

    ChamadoController.$inject = ['$scope', '$state', 'Chamado'];

    function ChamadoController ($scope, $state, Chamado) {
        var vm = this;
        
        vm.chamados = [];

        loadAll();

        function loadAll() {
            Chamado.query(function(result) {
                vm.chamados = result;
            });
        }
    }
})();
