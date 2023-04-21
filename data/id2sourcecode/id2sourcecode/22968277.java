    public void finalProcess(JSBuffer jsBuffer) throws PluginException {
        byte[] digest = _messageDigest.digest();
        int dataSize = 0;
        if (getProperty("outputFormat").equals("hex")) {
            dataSize = 32;
            digest = toHex(digest);
        } else {
            dataSize = 16;
        }
        jsBuffer.allocateNewBuffer(dataSize, digest);
    }
