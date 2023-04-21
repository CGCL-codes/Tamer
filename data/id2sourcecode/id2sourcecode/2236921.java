    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        token = diskCache.transferFromStream(stream);
    }
