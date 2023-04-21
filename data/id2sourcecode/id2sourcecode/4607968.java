    public String decryptData(byte[] data) {
        try {
            StaticDataHelper.log("" + key);
            DESDecryptorEngine decryptorEngine = new DESDecryptorEngine(key);
            PKCS5UnformatterEngine unformatterEngine = new PKCS5UnformatterEngine(decryptorEngine);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BlockDecryptor decryptor = new BlockDecryptor(unformatterEngine, inputStream);
            byte[] read = new byte[data.length];
            DataBuffer databuffer = new DataBuffer();
            int bytesRead = decryptor.read(read);
            if (bytesRead > 0) {
                databuffer.write(read, 0, bytesRead);
            }
            byte[] decryptedData = databuffer.toArray();
            return new String(decryptedData);
        } catch (CryptoTokenException e) {
            StaticDataHelper.log(e.toString());
        } catch (CryptoUnsupportedOperationException e) {
            StaticDataHelper.log(e.toString());
        } catch (IOException e) {
            StaticDataHelper.log(e.toString());
        }
        return null;
    }
