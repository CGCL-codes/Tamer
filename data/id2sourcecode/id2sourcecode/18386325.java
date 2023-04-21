    @Override
    public void write(DataOutput out) throws IOException {
        Text.writeString(out, location);
        out.writeByte(read);
    }
