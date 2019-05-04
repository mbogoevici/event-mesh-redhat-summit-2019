package com.redhat.fuse.samples.interconnect.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Metadata {

    private String connection;

    private String localRegion;

    private String accountUpdatesReceiveAddress;

    private String accountUpdatesSendAddress;

    private String liquidityBalanceTransfersReceiveAddress;

    private String liquidityBalanceTransfersSendLocalAddress;

    private String liquidityBalanceTransfersSendEmeaAddress;

    private String liquidityBalanceTransfersSendApacAddress;

    private String liquidityBalanceTransfersSendNaAddress;

    public Metadata() {
    }

    public Metadata(String connection, String localRegion, String accountUpdatesReceiveAddress, String accountUpdatesSendAddress, String liquidityBalanceTransfersReceiveAddress, String liquidityBalanceTransfersSendLocalAddress, String liquidityBalanceTransfersSendEmeaAddress, String liquidityBalanceTransfersSendApacAddress, String liquidityBalanceTransfersSendNaAddress) {
        this.connection = connection;
        this.localRegion = localRegion;
        this.accountUpdatesReceiveAddress = accountUpdatesReceiveAddress;
        this.accountUpdatesSendAddress = accountUpdatesSendAddress;
        this.liquidityBalanceTransfersReceiveAddress = liquidityBalanceTransfersReceiveAddress;
        this.liquidityBalanceTransfersSendLocalAddress = liquidityBalanceTransfersSendLocalAddress;
        this.liquidityBalanceTransfersSendEmeaAddress = liquidityBalanceTransfersSendEmeaAddress;
        this.liquidityBalanceTransfersSendApacAddress = liquidityBalanceTransfersSendApacAddress;
        this.liquidityBalanceTransfersSendNaAddress = liquidityBalanceTransfersSendNaAddress;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getLocalRegion() {
        return localRegion;
    }

    public void setLocalRegion(String localRegion) {
        this.localRegion = localRegion;
    }

    public String getAccountUpdatesReceiveAddress() {
        return accountUpdatesReceiveAddress;
    }

    public void setAccountUpdatesReceiveAddress(String accountUpdatesReceiveAddress) {
        this.accountUpdatesReceiveAddress = accountUpdatesReceiveAddress;
    }

    public String getAccountUpdatesSendAddress() {
        return accountUpdatesSendAddress;
    }

    public void setAccountUpdatesSendAddress(String accountUpdatesSendAddress) {
        this.accountUpdatesSendAddress = accountUpdatesSendAddress;
    }

    public String getLiquidityBalanceTransfersReceiveAddress() {
        return liquidityBalanceTransfersReceiveAddress;
    }

    public void setLiquidityBalanceTransfersReceiveAddress(String liquidityBalanceTransfersReceiveAddress) {
        this.liquidityBalanceTransfersReceiveAddress = liquidityBalanceTransfersReceiveAddress;
    }

    public String getLiquidityBalanceTransfersSendLocalAddress() {
        return liquidityBalanceTransfersSendLocalAddress;
    }

    public void setLiquidityBalanceTransfersSendLocalAddress(String liquidityBalanceTransfersSendLocalAddress) {
        this.liquidityBalanceTransfersSendLocalAddress = liquidityBalanceTransfersSendLocalAddress;
    }

    public String getLiquidityBalanceTransfersSendEmeaAddress() {
        return liquidityBalanceTransfersSendEmeaAddress;
    }

    public void setLiquidityBalanceTransfersSendEmeaAddress(String liquidityBalanceTransfersSendEmeaAddress) {
        this.liquidityBalanceTransfersSendEmeaAddress = liquidityBalanceTransfersSendEmeaAddress;
    }

    public String getLiquidityBalanceTransfersSendApacAddress() {
        return liquidityBalanceTransfersSendApacAddress;
    }

    public void setLiquidityBalanceTransfersSendApacAddress(String liquidityBalanceTransfersSendApacAddress) {
        this.liquidityBalanceTransfersSendApacAddress = liquidityBalanceTransfersSendApacAddress;
    }

    public String getLiquidityBalanceTransfersSendNaAddress() {
        return liquidityBalanceTransfersSendNaAddress;
    }

    public void setLiquidityBalanceTransfersSendNaAddress(String liquidityBalanceTransfersSendNaAddress) {
        this.liquidityBalanceTransfersSendNaAddress = liquidityBalanceTransfersSendNaAddress;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
