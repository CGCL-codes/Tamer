    public void setBankNum(int bankNum) {
        try {
            send(0xB0 + (getChannel() - 1), 0x20, bankNum + 5);
        } catch (Exception e) {
        }
        ;
    }
