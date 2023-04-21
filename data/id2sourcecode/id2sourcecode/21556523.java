    public void addKey(String key) throws IOException {
        reader.seek(reader.length());
        long pointer = reader.getFilePointer();
        reader.write(key.getBytes());
        reader.write("\n".getBytes());
        putKey(pointer, key);
    }
