    public void setObject(Serializable object) throws SpreadException {
        digestBytes = null;
        digestOutput = null;
        content = CONTENT_OBJECT;
        ByteArrayOutputStream objectBytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput;
        try {
            objectOutput = new ObjectOutputStream(objectBytes);
        } catch (IOException e) {
            throw new SpreadException("ObjectOutputStream(): " + e);
        }
        try {
            objectOutput.writeObject(object);
            objectOutput.flush();
        } catch (IOException e) {
            throw new SpreadException("writeObject/flush(): " + e);
        }
        data = objectBytes.toByteArray();
        try {
            objectOutput.close();
            objectBytes.close();
        } catch (IOException e) {
            throw new SpreadException("close/close(): " + e);
        }
    }
