package com.kopiright.tanit.noboiler;

import com.kopiright.compiler.base.TokenReference;

public class NBPageModeComponent extends NBPhylum {

    public NBPageModeComponent(TokenReference tokref, int lineNumber, String boiler) {
        super(tokref);
        this.lineNumber = lineNumber;
        this.boiler = boiler;
    }

    /**
   * Accepts the specified visitor
   * @param     v               the visitor
   */
    public void accept(NBVisitor v) {
        v.visitPageModeComponent(this);
    }

    /**
   * Get the value of lineNumber.
   * @return Value of lineNumber.
   */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
   * Set the value of lineNumber.
   * @param v  Value to assign to lineNumber.
   */
    public void setLineNumber(int v) {
        this.lineNumber = v;
    }

    /**
   * Get the value of boiler.
   * @return Value of boiler.
   */
    public String getBoiler() {
        return boiler;
    }

    /**
   * Set the value of boiler.
   * @param v  Value to assign to boiler.
   */
    public void setBoiler(String v) {
        this.boiler = v;
    }

    protected int lineNumber;

    protected String boiler;
}
