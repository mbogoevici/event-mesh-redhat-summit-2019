package com.redhat.fuse.samples.interconnect.core.datamodels.canonical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class AccountUpdateCommand {

    private String accountId;

    private String payload;

    private String region;

    private String type;

    private String partner;
    private String connection;


    public AccountUpdateCommand() {
    }

    public AccountUpdateCommand(String accountId, String payload, String region, String type) {
        this.accountId = accountId;
        this.payload = payload;
        this.region = region;
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getConnection() {
        return connection;
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