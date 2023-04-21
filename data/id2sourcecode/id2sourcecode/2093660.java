    public void decrypt() throws IOException, InvalidCipherTextException, NoSuchAlgorithmException {
        FileChannel channel = new FileInputStream(this.dbFile).getChannel();
        ByteBuffer content;
        try {
            ByteBuffer bb = ByteBuffer.allocate(Header.LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            channel.read(bb);
            this.header = new Header(bb);
            if ((header.getVersion() & 0xFFFFFF00) != (DB_VERSION & 0xFFFFFF00)) {
                throw new IOException("Unsupproted version: " + Integer.toHexString(this.header.getVersion()));
            }
            content = ByteBuffer.allocate((int) (channel.size() - channel.position())).order(ByteOrder.LITTLE_ENDIAN);
            channel.read(content);
        } finally {
            channel.close();
        }
        decryptContent(content.array());
        content.rewind();
        this.groups = new Group[this.header.getGroups()];
        for (int i = 0; i < this.groups.length; i++) {
            short fieldType;
            GroupBuilder builder = new GroupBuilder();
            while ((fieldType = content.getShort()) != -1) {
                if (fieldType == 0) {
                    continue;
                }
                int fieldSize = content.getInt();
                builder.readField(fieldType, fieldSize, content);
            }
            content.getInt();
            this.groups[i] = builder.buildGroup();
        }
        this.entries = new Entry[this.header.getEntries()];
        for (int i = 0; i < this.entries.length; i++) {
            short fieldType;
            EntryBuilder builder = new EntryBuilder();
            while ((fieldType = content.getShort()) != -1) {
                if (fieldType == 0) {
                    continue;
                }
                int fieldSize = content.getInt();
                builder.readField(fieldType, fieldSize, content);
            }
            content.getInt();
            this.entries[i] = builder.buildEntry();
        }
    }
