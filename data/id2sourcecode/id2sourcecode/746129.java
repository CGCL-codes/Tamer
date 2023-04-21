    public boolean Open() {
        if (xMode == -1) {
            errMessage = "Must set mode using setMode(read=0,write=1)";
            return false;
        } else {
            return Open(xMode);
        }
    }
