    @Override
    public final void copyToLocalFile(String destFile) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if (getLegacy().isEncrypted()) {
            FSDataInputStream fsDataInputStream = null;
            try {
                fsDataInputStream = dfs.open(realPath, blocksize);
            } catch (IOException e) {
                throw new ArFileException("File cannot be written", e);
            }
            Cipher cipherIn = getLegacy().getKeyObject().toDecrypt();
            if (cipherIn == null) {
                throw new ArFileException("CIpher uncorrect");
            }
            CipherInputStream cipherInputStream = new CipherInputStream(fsDataInputStream, cipherIn);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(destFile);
                byte[] bytes = new byte[blocksize];
                int read = 0;
                while (read >= 0) {
                    read = cipherInputStream.read(bytes);
                    if (read > 0) {
                        outputStream.write(bytes, 0, read);
                    }
                }
                cipherInputStream.close();
                cipherInputStream = null;
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            } catch (FileNotFoundException e) {
                throw new ArFileException("Error while copying", e);
            } catch (IOException e) {
                throw new ArFileException("Error while copying", e);
            } finally {
                if (cipherInputStream != null) {
                    try {
                        cipherInputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            Path dst = new Path(destFile);
            try {
                dfs.copyToLocalFile(realPath, dst);
            } catch (FileNotFoundException e) {
                throw new ArUnvalidIndexException("Cannot find document", e);
            } catch (IOException e) {
                throw new ArUnvalidIndexException("Cannot copy document", e);
            }
        }
    }
