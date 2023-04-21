    public void sendConfiguration(Biasgen biasgen) throws HardwareInterfaceException {
        if (biasgen.getPotArray() == null) {
            log.info("BiasgenUSBInterface.send(): iPotArray=null, no biases to send");
            return;
        }
        ensureOpen();
        byte[] dataBytes;
        if (biasgen.getPotArray() instanceof net.sf.jaer.biasgen.IPotArray) {
            dataBytes = getBiasBytes(biasgen);
        } else {
            VPot p = null;
            ArrayList<Pot> pots = biasgen.getPotArray().getPots();
            dataBytes = new byte[pots.size() * 3];
            int i = 0;
            for (Pot pot : pots) {
                p = (VPot) pot;
                dataBytes[i] = (byte) p.getChannel();
                dataBytes[i + 1] = (byte) ((p.getBitValue() & 0x0F00) >> 8);
                dataBytes[i + 2] = (byte) (p.getBitValue() & 0x00FF);
                i += 3;
            }
        }
        byte[] allBytes = new byte[2 + dataBytes.length];
        allBytes[0] = BIAS_BIASES;
        allBytes[1] = (byte) (0xff & dataBytes.length);
        System.arraycopy(dataBytes, 0, allBytes, 2, dataBytes.length);
        sendBytes(allBytes);
    }
