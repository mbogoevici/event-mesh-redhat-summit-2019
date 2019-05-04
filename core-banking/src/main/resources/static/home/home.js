'use strict';

angular.module('myApp.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html',
    controller: 'homeCtrl'
  });
}])

.controller('homeCtrl', ['$scope','$sce', 'notificationService', 'regionSvc', function($scope, $sce, notificationService, regionSvc) {

    var metadataPromise = regionSvc.getMetadata();
    metadataPromise.then(function (payload) {
        $scope.coreMetadata = payload.data;
    });

    var accountUpdatesPromise = regionSvc.getAccountUpdates();
    accountUpdatesPromise.then(
        function (payload) {
            $scope.accountUpdates = payload.data ? payload.data : [];
            notificationService.registerHandler("accountUpdate", function(e) {
                $scope.accountUpdates.push(JSON.parse(e.data))
                $scope.$apply()
            });
        }
    )

    var liquidityBalanceTransfersPromise = regionSvc.getLiquidityBalanceTransfers();
    liquidityBalanceTransfersPromise.then(
        function (payload) {
            $scope.liquidityBalanceTransfers = payload.data ? payload.data : [];
            notificationService.registerHandler("liquidityBalanceTransfer", function(e) {
                $scope.liquidityBalanceTransfers.push(JSON.parse(e.data))
                $scope.$apply()
            });
        }
    )

    $scope.account = {};

    $scope.liquidityBalanceTransfer = {};


    $scope.sendAccountUpdate = function (){
        $scope.account.region = $scope.coreMetadata.localRegion;
        regionSvc.sendAccountUpdate($scope.account)
        $scope.account = {};
    }

    $scope.sendLiquidityBalanceTransfer = function (toRegion){
        $scope.liquidityBalanceTransfer.fromRegion = $scope.coreMetadata.localRegion;
        regionSvc.sendLiquidityBalanceTransfer($scope.liquidityBalanceTransfer, $scope.liquidityBalanceTransfer.toRegion)
        $scope.liquidityBalanceTransfer = {};
    }


    $scope.clearSelection = function (event) {
        angular.element(event.currentTarget).removeClass('selected')
    }
}]);
