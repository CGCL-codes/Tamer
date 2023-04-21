    @Override
    public void sendConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        if (vpotValues == null) {
            vpotValues = new int[MAX_POTS];
            for (int i = 0; i < vpotValues.length; i++) {
                vpotValues[i] = -1;
            }
        }
        if (!onChipBiases) for (int i = 0; i < biasgen.getNumPots(); i++) {
            VPot vpot = (VPot) biasgen.getPotArray().getPotByNumber(i);
            int chan = vpot.getChannel();
            int mv = (int) (1000 * vpot.getVoltage());
            if (mv > 2500) mv += (int) (1000 * (VDD_IS - VDD_SHOULD_BE));
            if (vpotValues[chan] != mv) {
                vpotValues[chan] = mv;
                if (debugging) System.err.println("setting DAC channel " + i + " (" + vpot.getName() + ") to " + mv + " mV");
                sendCommand("DAC " + Integer.toHexString(i) + " " + Integer.toHexString((int) (mv * DSPIC_DAC_SCALEFACTOR)));
            }
        }
        if (ipotValues == null) {
            ipotValues = new int[12];
            for (int i = 0; i < ipotValues.length; i++) {
                ipotValues[i] = -1;
            }
        }
        Iterator<IPot> ipotsIt = (Iterator<IPot>) ((MDC2D.MDC2DBiasgen) biasgen).getShiftRegisterIterator();
        boolean allEqual = true;
        byte[] allbin = new byte[36];
        for (int i = 0; ipotsIt.hasNext(); i++) {
            IPot ipot = ipotsIt.next();
            int sr = ipot.getShiftRegisterNumber();
            if (ipotValues[sr] != ipot.getBitValue()) {
                ipotValues[sr] = ipot.getBitValue();
                allEqual = false;
            }
            byte[] bin = ipot.getBinaryRepresentation();
            allbin[sr * 3 + 0] = bin[0];
            allbin[sr * 3 + 1] = bin[1];
            allbin[sr * 3 + 2] = bin[2];
        }
        if (!allEqual && onChipBiases) {
            StringBuilder allValues = new StringBuilder();
            for (int i = 0; i < allbin.length; i++) allValues.append(String.format("%02X", allbin[i]));
            sendCommand("biases " + allValues.toString());
        }
    }
