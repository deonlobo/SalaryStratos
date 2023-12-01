package com.analysis.SalaryStratos.models;

public class CrawlerRequest {
    private boolean simplyHired;
    private boolean remoteOk;
    private boolean glassDoor;
    private boolean delete;
    private String[] searchTerms;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
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