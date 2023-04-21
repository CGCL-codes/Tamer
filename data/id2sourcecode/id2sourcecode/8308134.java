    public OutputType getChannelInputType(int idx) {
        System.err.println("Invalid channel access in " + this);
        return OutputType.SCALAR;
    }
