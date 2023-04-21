    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        myLockObject = new Object();
        serializedAstToken = diskCache.transferFromStream(stream);
    }
