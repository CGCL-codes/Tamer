    protected final void copyPathToPathInternal(ArkHadoopLegacy legacySrc, Path src, ArkHadoopLegacy legacyDst, Path dst, boolean useCipher) throws ArFileException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (src == null) {
            throw new ArFileException("Path uncorrect");
        }
        if (dst == null) {
            throw new ArFileException("Path uncorrect");
        }
        try {
            if ((!useCipher) || legacySrc.isLegacySharingKeyObject(legacyDst)) {
                FSDataInputStream fsDataInputStream = null;
                try {
                    fsDataInputStream = legacySrc.getFileSystem().open(src, ArConstants.BUFFERSIZEDEFAULT);
                } catch (IOException e) {
                    throw new ArFileException("File cannot be read", e);
                }
                inputStream = fsDataInputStream;
                FSDataOutputStream fsDataOutputStream = null;
                try {
                    fsDataOutputStream = legacyDst.getFileSystem().create(dst, false, ArConstants.BUFFERSIZEDEFAULT);
                } catch (IOException e) {
                    throw new ArFileException("File cannot be written", e);
                }
                outputStream = fsDataOutputStream;
            } else {
                inputStream = getInputStream(legacySrc, src, ArConstants.BUFFERSIZEDEFAULT);
                outputStream = getOutputStream(legacyDst, dst, ArConstants.BUFFERSIZEDEFAULT);
            }
            byte[] bytes = new byte[ArConstants.BUFFERSIZEDEFAULT];
            int read = 0;
            while (read >= 0) {
                read = inputStream.read(bytes);
                if (read > 0) {
                    outputStream.write(bytes, 0, read);
                }
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            throw new ArFileException("Error while copying", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                }
                inputStream = null;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                }
                outputStream = null;
            }
        }
    }
