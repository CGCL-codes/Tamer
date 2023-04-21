    public void sendConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        if (biasgen.getPotArray() == null) {
            log.info("BiasgenUSBInterface.send(): iPotArray=null, no biases to send");
            return;
        }
        ensureOpen();
        byte[] toSend;
        if (biasgen.getPotArray() instanceof net.sf.jaer.biasgen.IPotArray) {
            toSend = getBiasBytes(biasgen);
        } else {
            VPot p = null;
            ArrayList<Pot> pots = biasgen.getPotArray().getPots();
            toSend = new byte[pots.size() * 3];
            int i = 0;
            for (Pot pot : pots) {
                p = (VPot) pot;
                toSend[i] = (byte) p.getChannel();
                toSend[i + 1] = (byte) ((p.getBitValue() & 0x0F00) >> 8);
                toSend[i + 2] = (byte) (p.getBitValue() & 0x00FF);
                i += 3;
            }
        }
        int status = nativeSendBiases(toSend);
        if (status == 0) {
            HardwareInterfaceException.clearException();
            return;
        } else {
            close();
            throw new HardwareInterfaceException("nativeSendBiases: can't send biases: " + errorText(status));
        }
    }
