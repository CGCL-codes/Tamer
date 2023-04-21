package com.jcertif.web.ihm.home;

/**
 * Encapsulation of remaining time.
 * 
 * @author rossi.oddet
 * 
 */
public class RemainingTime {

    /** Centaine. **/
    private Character hundred;

    /** Dizaine **/
    private Character decade;

    /** Unité **/
    private Character unit;

    /**
	 * @return the hundred
	 */
    public Character getHundred() {
        return hundred;
    }

    /**
	 * @param hundred
	 *            the hundred to set
	 */
    public void setHundred(Character hundred) {
        this.hundred = hundred;
    }

    /**
	 * @return the decade
	 */
    public Character getDecade() {
        return decade;
    }

    /**
	 * @param decade
	 *            the decade to set
	 */
    public void setDecade(Character decade) {
        this.decade = decade;
    }

    /**
	 * @return the unit
	 */
    public Character getUnit() {
        return unit;
    }

    /**
	 * @param unit
	 *            the unit to set
	 */
    public void setUnit(Character unit) {
        this.unit = unit;
    }
}
