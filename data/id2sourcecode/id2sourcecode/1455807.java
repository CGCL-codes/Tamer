    @Override
    public void write(DataWriter out, Header header, IndexBlock indexBlock) {
        if (dds.isChanged()) {
            dds.write(out);
        } else {
            long returnTo = inBuffer.getFilePointer();
            inBuffer.seek(offset);
            out.writeChunk(inBuffer.readChunk(originalLength));
            inBuffer.seek(returnTo);
        }
    }
