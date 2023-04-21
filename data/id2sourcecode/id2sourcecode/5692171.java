    @Override
    public double getChannel(final double energy) {
        return ((energy - coeff[0]) / coeff[1]);
    }
