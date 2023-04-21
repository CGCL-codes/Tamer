    @Override
    public Field read(DataInputStream input) throws IOException {
        int size = this.getSerializedSize();
        byte[] bytes = new byte[size];
        if (input.read(bytes, 0, size) != size) {
            throw new CrappyDBMSError("Cant read from in-memory byte array");
        } else {
            byte stringLength = bytes[0];
            if (stringLength > this.getMaximumLength()) {
                throw new CrappyDBMSError("String length (" + stringLength + ") surpasses maximum length (" + this.getMaximumLength() + ")");
            }
            byte[] string = new byte[stringLength];
            for (int i = 0; i < stringLength; i++) {
                string[i] = bytes[i + 1];
            }
            return StringField.valueOf(new String(string));
        }
    }
