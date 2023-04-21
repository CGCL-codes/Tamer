    protected void writeData(AnalyseReader.Header hdr, JismContext ctx, String path) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(hdr.bitsPerPixel / 8);
        RandomAccessFile out = new RandomAccessFile(path, "rw");
        FileChannel channel = out.getChannel();
        out.setLength(0);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int z = 0; z < ctx.getDepth(); z++) for (int x = 0; x < ctx.getWidth(); x++) for (int y = 0; y < ctx.getHeight(); y++) {
            switch(hdr.datatype) {
                case DT_SIGNED_SHORT:
                    buffer.putShort(0, (short) ctx.getPixelIntensity(ctx.pixelId(x, y, z)));
                    break;
                case DT_SIGNED_INT:
                    buffer.putInt(0, ctx.getPixelIntensity(ctx.pixelId(x, y, z)));
                    break;
                case DT_FLOAT:
                    buffer.putFloat(0, (float) ctx.getPixelIntensity(ctx.pixelId(x, y, z)));
                    break;
                case DT_DOUBLE:
                    buffer.putDouble(0, (double) ctx.getPixelIntensity(ctx.pixelId(x, y, z)));
                    break;
                default:
                    throw new JismError("unsupported data type");
            }
            buffer.position(0);
            channel.write(buffer);
        }
        out.close();
    }
