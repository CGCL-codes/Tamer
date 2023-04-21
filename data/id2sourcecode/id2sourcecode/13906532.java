    private void reset() {
        if (this.getConnection().getChannel() == null) return;
        if (this.LastIp == 0) {
            this.getFrames().sendChatMessage(0, "Theres many places, for find. You just need to search them!");
        }
        String ip = "" + this.getConnection().getChannel().getLocalAddress();
        ip = ip.replaceAll("/", "");
        ip = ip.replaceAll(" ", "");
        ip = ip.substring(0, ip.indexOf(":"));
        this.setLastIp(Misc.IPAddressToNumber(ip));
        if (World.getIps().containsKey(this.LastIp)) World.getIps().remove(this.LastIp);
        World.getIps().put(this.LastIp, System.currentTimeMillis());
    }
