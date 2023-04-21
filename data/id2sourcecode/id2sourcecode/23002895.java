    public Send[] getAllSends() {
        Send[] allSends = new Send[0];
        for (int i = 0; i < this.getSubChannels().size(); i++) {
            Channel c = this.getSubChannels().getChannel(i);
            Send[] sends = c.getSends();
            if (sends.length == 0) {
                continue;
            }
            Send[] temp = new Send[allSends.length + sends.length];
            System.arraycopy(allSends, 0, temp, 0, allSends.length);
            System.arraycopy(sends, 0, temp, allSends.length, sends.length);
            allSends = temp;
        }
        for (int i = 0; i < this.getChannels().size(); i++) {
            Channel c = this.getChannels().getChannel(i);
            Send[] sends = c.getSends();
            if (sends.length == 0) {
                continue;
            }
            Send[] temp = new Send[allSends.length + sends.length];
            System.arraycopy(allSends, 0, temp, 0, allSends.length);
            System.arraycopy(sends, 0, temp, allSends.length, sends.length);
            allSends = temp;
        }
        return allSends;
    }
