    private static void writeIndexFile(String dbName, String snapName, int indexId, File targetFile, BabuDBVersionReader reader) throws IOException {
        OutputStream out = new FileOutputStream(targetFile);
        ByteBuffer lenBytes = ByteBuffer.wrap(new byte[4]);
        Iterator<Entry<byte[], byte[]>> it = snapName == null ? reader.getIndexContent(dbName, indexId) : reader.getIndexContent(dbName, snapName, indexId);
        while (it.hasNext()) {
            Entry<byte[], byte[]> next = it.next();
            lenBytes.putInt(next.getKey().length);
            lenBytes.position(0);
            out.write(lenBytes.array());
            out.write(next.getKey());
            lenBytes.putInt(next.getValue().length);
            lenBytes.position(0);
            out.write(lenBytes.array());
            out.write(next.getValue());
        }
        out.close();
    }
