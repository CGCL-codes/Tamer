    private void hashFile(FileListNode node) {
        int read;
        byte[] data;
        MessageDigest digest;
        FileInputStream stream;
        logger.log(Level.INFO, "LocalFilelist.hashTree hashing file: " + node.getFileName());
        try {
            data = new byte[512 * 1024];
            digest = MessageDigest.getInstance("SHA-256");
            stream = new FileInputStream(node.getLocalPath());
            while ((read = stream.read(data)) != -1) {
                digest.update(data, 0, read);
                activeHashSize += read;
                hashingSpeedValue += read;
            }
            node.setHash(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "LocalFilelist.hashTree: Failed to find hash instance");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "LocalFilelist.hashTree: File not found (" + node.getLocalPath() + ")");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "LocalFilelist.hashTree: Failed to hash file: " + node.getLocalPath());
        }
    }
