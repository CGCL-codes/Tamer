        @Override
        public synchronized void close() throws IOException {
            if (closed) {
                return;
            }
            backupStream.close();
            try {
                byte[] md5Hash = digest == null ? null : digest.digest();
                store.storeFile(key, backupFile, md5Hash);
            } finally {
                if (!backupFile.delete()) {
                    LOG.warn("Could not delete temporary s3n file: " + backupFile);
                }
                super.close();
                closed = true;
            }
        }
