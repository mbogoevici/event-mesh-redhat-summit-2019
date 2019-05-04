package com.redhat.fuse.samples.interconnect.core.datamodels.canonical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

public class LiquidityBalanceTransfer {

    private String fromRegion;

    private String fromAccount;

    private String toRegion;

    private String toAccount;

    private BigDecimal amount;

    public LiquidityBalanceTransfer() {
    }

    public LiquidityBalanceTransfer(String fromRegion, String fromAccount, String toRegion, String toAccount, BigDecimal amount) {
        this.fromRegion = fromRegion;
        this.fromAccount = fromAccount;
        this.toRegion = toRegion;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getFromRegion() {
        return fromRegion;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToRegion() {
        return toRegion;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
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
