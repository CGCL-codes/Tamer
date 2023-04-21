    @Override
    public final String storeFromLocalFile(String srcFile, String metadata) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        if (!canWrite()) {
            throw new ArFileWormException("Target doc already exists");
        }
        FilesystemBasedDigest digest = null;
        try {
            digest = new FilesystemBasedDigest(Configuration.algoMark);
        } catch (NoSuchAlgorithmException e1) {
            throw new ArFileException("Cannot create Digest", e1);
        }
        FSDataOutputStream fsDataOutputStream = null;
        CipherOutputStream cipherOutputStream = null;
        FileInputStream inputStream = null;
        try {
            writeMetadata(metadata);
            if (getLegacy().isEncrypted()) {
                try {
                    fsDataOutputStream = dfs.create(realPath, false, blocksize);
                } catch (IOException e) {
                    throw new ArFileException("File cannot be written", e);
                }
                Cipher cipherOut = getLegacy().getKeyObject().toCrypt();
                if (cipherOut == null) {
                    throw new ArFileException("CIpher uncorrect");
                }
                cipherOutputStream = new CipherOutputStream(fsDataOutputStream, cipherOut);
                position = 0;
                inputStream = new FileInputStream(srcFile);
                byte[] bytes = new byte[blocksize];
                int read = 0;
                while (read >= 0) {
                    read = inputStream.read(bytes);
                    if (read > 0) {
                        cipherOutputStream.write(bytes, 0, read);
                        digest.Update(bytes, 0, read);
                        position += read;
                    }
                }
                inputStream.close();
                inputStream = null;
                cipherOutputStream.flush();
                cipherOutputStream.close();
                cipherOutputStream = null;
                fsDataOutputStream = null;
            } else {
                digest = null;
                Path src = new Path(srcFile);
                dfs.copyFromLocalFile(src, realPath);
                File file = new File(srcFile);
                position = file.length();
            }
        } catch (IOException e) {
            deleteInternal(false);
            throw new ArFileException("Cannot write document", e);
        } finally {
            if (fsDataOutputStream != null) {
                try {
                    fsDataOutputStream.close();
                } catch (IOException e) {
                }
            }
            if (cipherOutputStream != null) {
                try {
                    cipherOutputStream.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        ArkAbstractUpdateTimeQueue.add(this);
        if (digest != null) {
            DKey = FilesystemBasedDigest.getHex(digest.Final());
        } else {
            DKey = this.getDKey();
        }
        if (DKey == null) {
            this.deleteInternal(false);
            throw new ArFileException("Cannot get DKey");
        }
        return DKey;
    }
