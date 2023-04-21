    public void write(Kryo kryo, Output output, Object object) {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        CipherOutputStream cipherStream = new CipherOutputStream(output, cipher);
        Output cipherOutput = new Output(cipherStream, 256) {

            public void close() throws KryoException {
            }
        };
        kryo.writeObject(cipherOutput, object, serializer);
        cipherOutput.flush();
        try {
            cipherStream.close();
        } catch (IOException ex) {
            throw new KryoException(ex);
        }
    }
