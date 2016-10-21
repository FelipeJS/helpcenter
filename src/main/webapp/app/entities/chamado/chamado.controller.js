(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoController', ChamadoController);

    ChamadoController.$inject = ['$scope', '$state', 'DataUtils', 'Chamado'];

    function ChamadoController ($scope, $state, DataUtils, Chamado) {
        var vm = this;
        
        vm.chamados = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Chamado.query(function(result) {
                vm.chamados = result;
            });
        }
    }
})();
