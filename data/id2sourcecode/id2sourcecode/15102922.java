    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(Id);
        out.writeInt(Number);
        From.writeTo(out);
        To.writeTo(out);
        out.writeString(Subject, 72);
        out.writeLong(MsgTime);
        out.writeInt(Reference);
        out.writeShort(MsgFlags);
        out.writeInt(MsgSize);
        out.writeInt(Conference);
        out.writeInt(PrevUnread);
        out.writeInt(NextUnread);
        out.writeString(Attachment, 16);
    }
