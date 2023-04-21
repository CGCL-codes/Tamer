    public static HashFile createHashFile(File sharedFile, String algorithm, int chunkSize) throws IOException, NoSuchAlgorithmException {
        HashFile hashFile = new HashFile();
        InputStream in = new BufferedInputStream(new FileInputStream(sharedFile));
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] chunk = new byte[chunkSize];
        long fileSize = 0;
        int numBytesRead = in.read(chunk);
        while (numBytesRead != -1) {
            fileSize += numBytesRead;
            Hash hash = Hash.createHash(chunk, numBytesRead, algorithm);
            hashFile.chunkHashes.add(hash);
            md.update(chunk);
            numBytesRead = in.read(chunk);
        }
        hashFile.file = sharedFile;
        hashFile.fileId = new Hash(md.digest());
        hashFile.fileSize = fileSize;
        hashFile.chunkSize = chunkSize;
        hashFile.hashAlgorithm = algorithm;
        hashFile.writeToFile();
        return hashFile;
    }
