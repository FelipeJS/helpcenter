(function() {
    'use strict';

    angular
        .module('helpcenterApp')
        .controller('ChamadoDialogController', ChamadoDialogController);

    ChamadoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Chamado', 'User'];

    function ChamadoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Chamado, User) {
        var vm = this;

        vm.chamado = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chamado.id !== null) {
                Chamado.update(vm.chamado, onSaveSuccess, onSaveError);
            } else {
                Chamado.save(vm.chamado, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('helpcenterApp:chamadoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setAnexo = function ($file, chamado) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        chamado.anexo = base64Data;
                        chamado.anexoContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.dataDeAbertura = false;
        vm.datePickerOpenStatus.dataDeFechamento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
