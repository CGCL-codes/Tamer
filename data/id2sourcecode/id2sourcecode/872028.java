    public void callback() {
        try {
            chan0 = bs.getChannel(0);
            chan1 = bs.getChannel(1);
            System.out.println("Chan 0 = " + chan0 + " : Chan 1 = " + chan1 + " : ");
            int side = decode(chan0, chan1);
            if (side < 0) System.out.println("not a side"); else sendEvent(side);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
