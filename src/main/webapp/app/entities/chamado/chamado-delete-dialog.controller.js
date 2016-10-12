(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoDeleteController',ChamadoDeleteController);

    ChamadoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Chamado'];

    function ChamadoDeleteController($uibModalInstance, entity, Chamado) {
        var vm = this;

        vm.chamado = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Chamado.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
