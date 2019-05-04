angular.module('myApp.services', [])

.service ('regionSvc', function ($http){

    var getAccountUpdates = function (){
        return $http.get("camel/accountUpdates" );
    }

    var sendAccountUpdate = function (account){
        return $http.post("camel/accountUpdates", account);
    }

    var getLiquidityBalanceTransfers = function (){
        return $http.get("camel/liquidityBalanceTransfers" );
    }

    var sendLiquidityBalanceTransfer = function (account, toRegion){
        // deep copy so we can remove the id field on the published data
        return $http.post("camel/liquidityBalanceTransfers/" + toRegion, account)
    }

    var getMetadata = function () {
        return $http.get("camel/metadata");
    }

    return {
        getAccountUpdates : getAccountUpdates,
        sendAccountUpdate : sendAccountUpdate,
        getLiquidityBalanceTransfers : getLiquidityBalanceTransfers,
        sendLiquidityBalanceTransfer: sendLiquidityBalanceTransfer,
        getMetadata : getMetadata
    }
})

.service('notificationService', function () {

    var eventServiceUrl = 'eventStream'

    const eventSource = new EventSource(eventServiceUrl)

    var registerHandler = function (eventName, handler) {
        eventSource.addEventListener(eventName, handler, false)
    }

    return {
        registerHandler: registerHandler
    }

})