    public static Command getCommand(Connection conn) throws IOException, InterruptedException {
        byte[] cmdData = null;
        FlapHeader hdr = null;
        SNACPacket pkt = null;
        List<TLV> tlvs = null;
        try {
            hdr = FlapHeader.getHeader(conn);
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        }
        if (hdr != null) {
            int dataLength = hdr.getDataLength();
            cmdData = new byte[dataLength];
            int count = 0;
            while (count != cmdData.length) {
                try {
                    count += conn.read(cmdData, count, cmdData.length - count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (hdr.getChannelId() == FlapConstants.FLAP_CHANNEL_SNAC) {
                pkt = new SNACPacket(cmdData);
                cmdData = null;
            } else if (hdr.getChannelId() == FlapConstants.FLAP_CHANNEL_DISCONNECT) {
                tlvs = TLV.getTLVs(cmdData, 0);
            }
        }
        Command cmd = null;
        if (hdr != null) {
            cmd = new BaseCommand(hdr, pkt, tlvs, cmdData);
        }
        return cmd;
    }
