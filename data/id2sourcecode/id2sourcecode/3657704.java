    public void run() {
        int frame_count = 0;
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 2);
        try {
            if (objHeader.syncFrame() == false) return;
            objDec123 = new Decoder(new BitStream(objIRA), objHeader, Decoder.CH_BOTH);
            Audio.open(objHeader.getFrequency(), 16, objHeader.getChannels());
            while (true) {
                if (objDec123.decodeFrame() == false) break;
                if (objHeader.syncFrame() == false) break;
                if ((++frame_count & 0x7) == 0x7) objHeader.printState();
            }
            objHeader.printState();
            Audio.close();
        } catch (Exception e) {
        }
        objIRA.close();
    }
