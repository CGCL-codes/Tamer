    private int getPseudoControl(int control) {
        switch(control) {
            case PITCH_BEND_PSEUDO:
                return getPitchBend();
            case POLY_PRESSURE_PSEUDO:
                return 0;
            case CHANNEL_PRESSURE_PSEUDO:
                return getChannelPressure();
            case PROGRAM_PSEUDO:
                return getProgram();
        }
        return 0;
    }
