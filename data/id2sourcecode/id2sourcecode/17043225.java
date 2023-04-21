    @Override
    public ChannelBuffer write() {
        ChannelBuffer out = ChannelBuffers.dynamicBuffer();
        out.writeInt(0);
        out.writeInt(records.size());
        for (STSDRecord record : records) {
            ChannelBuffer desc = record.sampleDescription.write();
            out.writeInt(8 + desc.readableBytes());
            out.writeBytes(record.type.name().toLowerCase().getBytes());
            out.writeBytes(desc);
        }
        return out;
    }
