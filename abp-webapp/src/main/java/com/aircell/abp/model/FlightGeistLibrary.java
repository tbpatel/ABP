package com.aircell.abp.model;

import java.util.ArrayList;
import java.util.List;

public class FlightGeistLibrary {

    /**. private class variable to hold aaFlightGeist */
    private FlightGeist aaFlightGeist;
    /**. private class variable to hold generalFlightGeist */
    private FlightGeist generalFlightGeist;
    /**. private class variable to hold gogoFlightGeist */
    private FlightGeist gogoFlightGeist;
    /**. private class variable to hold losanglesFlightGeist */
    private FlightGeist losanglesFlightGeist;
    /**. private class variable to hold miamiFlightGeist */
    private FlightGeist miamiFlightGeist;
    /**. private class variable to hold newyorkFlightGeist */
    private FlightGeist newyorkFlightGeist;
    /**. private class variable to hold sanfranciscoFlightGeist */
    private FlightGeist sanfranciscoFlightGeist;

    /**.
     * Default constructor
     *
     */
    public FlightGeistLibrary() {
    }

    /**.
     * Gets AaFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getAaFlightGeist() {
        return this.aaFlightGeist;
    }

    /**.
     * Sets AaFlightGeist
     * @param aaFlightGeist
     */
    public void setAaFlightGeist(FlightGeist aaFlightGeist) {
        this.aaFlightGeist = aaFlightGeist;
    }

    /**.
     * Gets GeneralFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getGeneralFlightGeist() {
        return this.generalFlightGeist;
    }

    /**.
     * Sets GeneralFlightGeist
     * @param generalFlightGeist
     */
    public void setGeneralFlightGeist(FlightGeist generalFlightGeist) {
        this.generalFlightGeist = generalFlightGeist;
    }

    /**.
     * Gets GogoFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getGogoFlightGeist() {
        return this.gogoFlightGeist;
    }

    /**.
     * Sets GogoFlightGeist
     * @param gogoFlightGeist
     */
    public void setGogoFlightGeist(FlightGeist gogoFlightGeist) {
        this.gogoFlightGeist = gogoFlightGeist;
    }

    /**.
     * Gets LosanglesFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getLosanglesFlightGeist() {
        return this.losanglesFlightGeist;
    }

    /**.
     * Sets LosanglesFlightGeist
     * @param losanglesFlightGeist
     */
    public void setLosanglesFlightGeist(FlightGeist losanglesFlightGeist) {
        this.losanglesFlightGeist = losanglesFlightGeist;
    }

    /**.
     * Gets MiamiFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getMiamiFlightGeist() {
        return this.miamiFlightGeist;
    }

    /**.
     * Sets MiamiFlightGeist
     * @param miamiFlightGeist
     */
    public void setMiamiFlightGeist(FlightGeist miamiFlightGeist) {
        this.miamiFlightGeist = miamiFlightGeist;
    }

    /**.
     * Gets NewyorkFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getNewyorkFlightGeist() {
        return this.newyorkFlightGeist;
    }

    /**.
     * Sets NewyorkFlightGeist
     * @param newyorkFlightGeist
     */
    public void setNewyorkFlightGeist(FlightGeist newyorkFlightGeist) {
        this.newyorkFlightGeist = newyorkFlightGeist;
    }

    /**.
     * Gets SanfranciscoFlightGeist
     * @return FlightGeist
     */
    public FlightGeist getSanfranciscoFlightGeist() {
        return this.sanfranciscoFlightGeist;
    }

    /**.
     * Sets SanfranciscoFlightGeist
     * @param sanfranciscoFlightGeist
     */
    public void setSanfranciscoFlightGeist(
    FlightGeist sanfranciscoFlightGeist
    ) {
        this.sanfranciscoFlightGeist = sanfranciscoFlightGeist;
    }

    /**.
     * Gets FlightGeistFacts
     * @param airline airline name
     * @param destination destination airport
     * @return List
     */
    public List<String> getFlightGeistFacts(
    String airline, String destination
    ) {
        ArrayList<String> flightGeistFacts = new ArrayList<String>();

        FlightGeist flightGeist = null;

        if (airline != null && airline.equalsIgnoreCase("AA")) {
            flightGeist = getAaFlightGeist();
            addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        }
        flightGeist = getGeneralFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        flightGeist = getGogoFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        flightGeist = getLosanglesFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        flightGeist = getMiamiFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        flightGeist = getNewyorkFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);
        flightGeist = getSanfranciscoFlightGeist();
        addFlightGeistFacts(flightGeistFacts, flightGeist, destination);

        return flightGeistFacts;
    }

    /**.
     * Adds FlightGeistFacts
     * @param flightGeistFacts
     * @param flightGeist flightGeist
     * @param destination destination
     */
    private void addFlightGeistFacts(
    List<String> flightGeistFacts, FlightGeist flightGeist, String destination
    ) {
        if (flightGeist == null) {
            return;
        }

        destination = destination == null ? "" : destination;

        if (!"destinationCity".equalsIgnoreCase(flightGeist.getType()) || (
        "destinationCity".equalsIgnoreCase(flightGeist.getType()) && flightGeist
        .getGroup().equalsIgnoreCase(destination.replaceAll(" ", "")))) {

            List<String> list = flightGeist.getFacts();
            if (list != null) {
                flightGeistFacts.addAll(list);
            }
        }
    }
}
