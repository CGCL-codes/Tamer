    public OutputType getChannelInputType(int idx) {
        if (idx == 0) return OutputType.SCALAR; else System.err.println("Invalid channel access in " + this);
        return OutputType.SCALAR;
    }
