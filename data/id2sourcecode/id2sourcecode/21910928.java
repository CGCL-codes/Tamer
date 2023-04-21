    public OutputType getChannelInputType(int idx) {
        if (idx == 0) return OutputType.RGBA; else if (idx == 1) return OutputType.RGBA; else System.err.println("Invalid channel access in " + this);
        return OutputType.SCALAR;
    }
