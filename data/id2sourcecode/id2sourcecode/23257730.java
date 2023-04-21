    public void write(OutputStream out) throws IOException {
        DataOutputStream data = new DataOutputStream(out);
        data.writeInt(packetSize);
        data.writeInt(address);
        data.writeInt(length);
        if (buffer instanceof ByteBuffer) {
            WritableByteChannel channel = Channels.newChannel(out);
            channel.write((ByteBuffer) buffer);
        } else {
            IMemoryReader reader = MemoryReader.getMemoryReader(address, length, 1);
            for (int i = 0; i < length; i++) {
                data.writeByte(reader.readNext());
            }
        }
        VideoEngine.log.info(String.format("Saved memory %08x - %08x (len %08x)", address, address + length, length));
    }
