    public byte[] hashFileContent(File target, TaskMonitor monitor) throws IOException, NoSuchAlgorithmException, TaskCancelledException {
        InputStream is = null;
        try {
            is = FileSystemManager.getFileInputStream(target);
            MessageDigest dg = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] buff = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(buff)) != -1) {
                if (monitor != null) {
                    monitor.checkTaskState();
                }
                dg.update(buff, 0, len);
            }
            return dg.digest();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
