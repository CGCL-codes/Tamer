    public String toString() {
        return "OT " + otype + " OI " + oindex + " PID " + id + " PI " + pindex + " PDT " + (pdt == -1 ? "-" : Integer.toString(getPDT())) + ", elements (curr/max) " + currElems + "/" + maxElems + ", access level (r/w) " + rLevel + "/" + wLevel + (write ? " write-enabled" : " read-only");
    }
