    @Override
    public double getChannel(final double energy) {
        return ((Math.sqrt(energy) - coeff[0]) / coeff[1]);
    }
