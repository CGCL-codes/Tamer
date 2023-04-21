    protected void handleFlapPacket(FlapPacketEvent e) {
        FlapCommand cmd = e.getFlapCommand();
        if (cmd instanceof LoginFlapCmd) {
            getFlapProcessor().sendFlap(new LoginFlapCmd(cookie));
        } else {
            System.out.println("got FLAP command on channel 0x" + Integer.toHexString(e.getFlapPacket().getChannel()) + ": " + cmd);
        }
    }
