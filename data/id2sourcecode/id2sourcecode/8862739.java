    @Override
    public void sendConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        PotArray potArray = biasgen.getPotArray();
        if (vpotValues == null) {
            vpotValues = new int[MAX_POTS];
            for (int i = 0; i < vpotValues.length; i++) {
                vpotValues[i] = -1;
            }
        }
        for (short i = 0; i < biasgen.getNumPots(); i++) {
            VPot vpot = (VPot) potArray.getPotByNumber(i);
            int chan = vpot.getChannel();
            if (vpotValues[chan] != vpot.getBitValue()) {
                sendVendorRequest(VENDOR_REQUEST_SEND_BIAS, (short) vpot.getBitValue(), (short) chan);
                vpotValues[chan] = vpot.getBitValue();
                log.info("set VPot value " + vpot.getBitValue() + " (" + vpot.getPhysicalValue() + vpot.getPhysicalValueUnits() + ") for channel " + chan);
            }
        }
        if (ipotValues == null) {
            ipotValues = new int[38];
            for (int i = 0; i < ipotValues.length; i++) {
                ipotValues[i] = -1;
            }
        }
        PotArray ipots = ((MDC2D.MDC2DBiasgen) biasgen).getIPotArray();
        for (short i = 0; i < ipots.getNumPots(); i++) {
            IPot ipot = (IPot) ipots.getPotByNumber(i);
            int chan = ipot.getShiftRegisterNumber();
            if (ipotValues[chan] != ipot.getBitValue()) {
                ipotValues[chan] = ipot.getBitValue();
                byte[] bin = ipot.getBinaryRepresentation();
                byte request = VENDOR_REQUEST_SEND_ONCHIP_BIAS;
                short value = (short) (((chan << 8) & 0xFF00) | ((bin[0]) & 0x00FF));
                short index = (short) (((bin[1] << 8) & 0xFF00) | (bin[2] & 0x00FF));
                sendVendorRequest(request, value, index);
                log.info("set IPot value " + ipot.getBitValue() + " (" + ipot.getPhysicalValue() + ipot.getPhysicalValueUnits() + ") into SR pos " + chan);
            }
        }
    }
