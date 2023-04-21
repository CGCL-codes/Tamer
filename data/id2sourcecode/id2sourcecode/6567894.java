    @Override
    protected double embouchureCorrection(final Whistle whistle) {
        final double embHeight = whistle.wallThickness;
        final double bore = whistle.bore;
        final double embWidth = whistle.embouchure.width;
        final double embLength = whistle.embouchure.length;
        final double Bd = (bore * bore) / (embWidth * embLength);
        final double De = (embWidth + embLength) / 2;
        return Bd * (embHeight + 0.3 * De);
    }
