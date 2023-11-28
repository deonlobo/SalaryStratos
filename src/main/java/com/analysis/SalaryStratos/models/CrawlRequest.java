package com.analysis.SalaryStratos.models;

import java.util.List;

public class CrawlRequest {
    private boolean simplyHired;
    private boolean remoteOk;
    private boolean glassDoor;
    private String[] searchTerms;
    public boolean isRemoteOk() {
        return remoteOk;
    }

    public String[] getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String[] searchTerms) {
        this.searchTerms = searchTerms;
    }

    public void setRemoteOk(boolean remoteOk) {
        this.remoteOk = remoteOk;
    }

    public boolean isGlassDoor() {
        return glassDoor;
    }

    public void setGlassDoor(boolean glassDoor) {
        this.glassDoor = glassDoor;
    }

    public boolean isSimplyHired() {
        return simplyHired;
    }

    public void setSimplyHired(boolean simplyHired) {
        this.simplyHired = simplyHired;
    }
}