    private void writeDetailInfo(DataOutputStream out) throws IOException {
        out.write("DINFACK".getBytes());
        DetailInfo info = player.getDetailInfo();
        out.writeInt(info.getBitrate());
        out.writeInt(info.getSamplRate());
        out.writeInt(info.getChannel());
    }
