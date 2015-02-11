package com.aircell.abp.model;

import java.util.List;

public class FlightGeist {

    /**. private class variable to hold type */
    private String type;
    /**. private class variable to hold group */
    private String group;
    /**. private class variable to hold facts */
    private List<String> facts;

    /**.
     * Default constructor
     */
    public FlightGeist() {
    }

    /**.
     * Gets Type
     * @return String type
     */
    public String getType() {
        return this.type;
    }

    /**.
     * Sets Type
     * @param type Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**.
     * Gets Group
     * @return String group
     */
    public String getGroup() {
        return this.group;
    }

    /**.
     * Sets Group
     * @param group Group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**.
     * Gets Facts
     * @return List facts
     */
    public List<String> getFacts() {
        return this.facts;
    }

    /**.
     * Sets Facts
     * @param facts Facts
     */
    public void setFacts(List<String> facts) {
        this.facts = facts;
    }
}
