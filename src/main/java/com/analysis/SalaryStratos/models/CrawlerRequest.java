package com.analysis.SalaryStratos.models;

public class CrawlerRequest {
    // Flags indicating whether to crawl data from specific job search platforms.
    private boolean simplyHired;
    private boolean remoteOk;
    private boolean glassDoor;

    // Flag indicating whether to delete existing data before crawling.
    private boolean delete;

    // Array of search terms to use in web scraping.
    private String[] searchTerms;

    // Getter and setter methods for the 'delete' property.

    /**
     * Gets the 'delete' flag indicating whether to delete existing data before crawling.
     *
     * @return True if data deletion is requested, false otherwise.
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Sets the 'delete' flag to indicate whether to delete existing data before crawling.
     *
     * @param delete True if data deletion is requested, false otherwise.
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    // Getter and setter methods for the 'remoteOk' property.

    /**
     * Gets the 'remoteOk' flag indicating whether to crawl data from Remote Ok platform.
     *
     * @return True if Remote Ok crawling is requested, false otherwise.
     */
    public boolean isRemoteOk() {
        return remoteOk;
    }

    /**
     * Sets the 'remoteOk' flag to indicate whether to crawl data from Remote Ok platform.
     *
     * @param remoteOk True if Remote Ok crawling is requested, false otherwise.
     */
    public void setRemoteOk(boolean remoteOk) {
        this.remoteOk = remoteOk;
    }

    // Getter and setter methods for the 'glassDoor' property.

    /**
     * Gets the 'glassDoor' flag indicating whether to crawl data from Glassdoor platform.
     *
     * @return True if Glassdoor crawling is requested, false otherwise.
     */
    public boolean isGlassDoor() {
        return glassDoor;
    }

    /**
     * Sets the 'glassDoor' flag to indicate whether to crawl data from Glassdoor platform.
     *
     * @param glassDoor True if Glassdoor crawling is requested, false otherwise.
     */
    public void setGlassDoor(boolean glassDoor) {
        this.glassDoor = glassDoor;
    }

    // Getter and setter methods for the 'simplyHired' property.

    /**
     * Gets the 'simplyHired' flag indicating whether to crawl data from SimplyHired platform.
     *
     * @return True if SimplyHired crawling is requested, false otherwise.
     */
    public boolean isSimplyHired() {
        return simplyHired;
    }

    /**
     * Sets the 'simplyHired' flag to indicate whether to crawl data from SimplyHired platform.
     *
     * @param simplyHired True if SimplyHired crawling is requested, false otherwise.
     */
    public void setSimplyHired(boolean simplyHired) {
        this.simplyHired = simplyHired;
    }

    // Getter and setter methods for the 'searchTerms' property.

    /**
     * Gets the array of search terms to be used in web scraping.
     *
     * @return Array of search terms.
     */
    public String[] getSearchTerms() {
        return searchTerms;
    }

    /**
     * Sets the array of search terms to be used in web scraping.
     *
     * @param searchTerms Array of search terms.
     */
    public void setSearchTerms(String[] searchTerms) {
        this.searchTerms = searchTerms;
    }
}
