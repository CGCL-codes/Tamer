    public static void main(String[] args) {
        String[] adapters = LP5DSMSerialTransmitter.getAvailableSerialAdapters();
        for (int i = 0; i < adapters.length; i++) {
            System.out.println("Adapter:" + i + "  Description: " + adapters[i]);
        }
        LP5DSMSerialTransmitter tx = new LP5DSMSerialTransmitter();
        for (int i = 0; i < 6; i++) {
            int minBefore = tx.getChannelRangeMin(i);
            int maxBefore = tx.getChannelRangeMax(i);
            tx.setChannelRange(i, minBefore, maxBefore);
            int minAfter = tx.getChannelRangeMin(i);
            int maxAfter = tx.getChannelRangeMax(i);
            System.out.println("Channel:" + i + " Min:" + minBefore + " Max:" + maxBefore + " Min:" + minAfter + " Max:" + maxAfter);
        }
    }
