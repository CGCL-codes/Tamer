    private SyncResult doPushFile(String localPath, String remotePath, ISyncProgressMonitor monitor) {
        FileInputStream fis = null;
        byte[] msg;
        final int timeOut = DdmPreferences.getTimeOut();
        try {
            byte[] remotePathContent = remotePath.getBytes(AdbHelper.DEFAULT_ENCODING);
            if (remotePathContent.length > REMOTE_PATH_MAX_LENGTH) {
                return new SyncResult(RESULT_REMOTE_PATH_LENGTH);
            }
            File f = new File(localPath);
            if (f.exists() == false) {
                return new SyncResult(RESULT_NO_LOCAL_FILE);
            }
            fis = new FileInputStream(f);
            msg = createSendFileReq(ID_SEND, remotePathContent, 0644);
        } catch (UnsupportedEncodingException e) {
            return new SyncResult(RESULT_REMOTE_PATH_ENCODING, e);
        } catch (FileNotFoundException e) {
            return new SyncResult(RESULT_FILE_READ_ERROR, e);
        }
        try {
            AdbHelper.write(mChannel, msg, -1, timeOut);
        } catch (IOException e) {
            return new SyncResult(RESULT_CONNECTION_ERROR, e);
        }
        if (mBuffer == null) {
            mBuffer = new byte[SYNC_DATA_MAX + 8];
        }
        System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);
        while (true) {
            if (monitor.isCanceled() == true) {
                return new SyncResult(RESULT_CANCELED);
            }
            int readCount = 0;
            try {
                readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);
            } catch (IOException e) {
                return new SyncResult(RESULT_FILE_READ_ERROR, e);
            }
            if (readCount == -1) {
                break;
            }
            ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);
            try {
                AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);
            } catch (IOException e) {
                return new SyncResult(RESULT_CONNECTION_ERROR, e);
            }
            monitor.advance(readCount);
        }
        try {
            fis.close();
        } catch (IOException e) {
            return new SyncResult(RESULT_FILE_READ_ERROR, e);
        }
        try {
            long time = System.currentTimeMillis() / 1000;
            msg = createReq(ID_DONE, (int) time);
            AdbHelper.write(mChannel, msg, -1, timeOut);
            byte[] result = new byte[8];
            AdbHelper.read(mChannel, result, -1, timeOut);
            if (checkResult(result, ID_OKAY) == false) {
                if (checkResult(result, ID_FAIL)) {
                    int len = ArrayHelper.swap32bitFromArray(result, 4);
                    AdbHelper.read(mChannel, mBuffer, len, timeOut);
                    String message = new String(mBuffer, 0, len);
                    Log.e("ddms", "transfer error: " + message);
                    return new SyncResult(RESULT_UNKNOWN_ERROR, message);
                }
                return new SyncResult(RESULT_UNKNOWN_ERROR);
            }
        } catch (IOException e) {
            return new SyncResult(RESULT_CONNECTION_ERROR, e);
        }
        return new SyncResult(RESULT_OK);
    }
