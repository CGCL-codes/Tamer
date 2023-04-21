    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(Conf);
        out.writeInt(HiMsg);
        out.writeInt(HiMsgId);
        out.writeInt(LoMsg);
        out.writeInt(LoMsgId);
        out.writeInt(LastRead);
        out.writeInt(FirstUnread);
        out.writeInt(ReadFlags);
    }
