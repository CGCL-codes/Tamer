    public void write(DataOutputStream stream) throws IOException {
        stream.writeBoolean(read);
        stream.writeLong(time);
        writeString(stream, contactName);
        writeString(stream, contactNumber);
        writeString(stream, text);
    }
