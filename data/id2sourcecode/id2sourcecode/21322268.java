    public void digest(Serializable object) throws SpreadException {
        if (content != CONTENT_DIGEST) {
            content = CONTENT_DIGEST;
            digestBytes = new ByteArrayOutputStream();
            try {
                digestOutput = new ObjectOutputStream(digestBytes);
            } catch (IOException e) {
                throw new SpreadException("ObjectOutputStream(): " + e);
            }
        }
        try {
            digestOutput.writeObject(object);
            digestOutput.flush();
        } catch (IOException e) {
            throw new SpreadException("writeObject/flush(): " + e);
        }
    }
