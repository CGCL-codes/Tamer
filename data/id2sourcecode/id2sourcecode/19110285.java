    public void setBankNum(int bankNum) {
        try {
            send(0xB0 + (getChannel() - 1), 0x00, 85);
            send(0xB0 + (getChannel() - 1), 0x20, 0);
        } catch (Exception e) {
        }
        ;
    }
