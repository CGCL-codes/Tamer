    public void setPatchNum(int patchNum) {
        try {
            send(0xC0 + (getChannel() - 1), patchNum);
        } catch (Exception e) {
        }
        ;
    }
