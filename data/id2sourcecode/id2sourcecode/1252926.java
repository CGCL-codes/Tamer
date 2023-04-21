    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(Number);
        out.writeString(Name, 60);
        out.writeString(ConfSysop, 72);
        out.writeByte(MailType);
        out.writeInt(HiMsg);
        out.writeInt(HiMsgId);
        out.writeInt(LoMsg);
        out.writeInt(LoMsgId);
        out.writeInt(LastRead);
        out.writeInt(ConfAccess);
        out.writeInt(ConfFlags);
        out.writeInt(ValidNames);
        out.writeInt(FirstUnread);
        out.writeInt(ReadFlags);
    }
