        public void run() {
            OutputStream os = null;
            InputStream is = null;
            File tmp = null;
            try {
                tmp = File.createTempFile(f.getName(), ".tmp", f.getParentFile());
                os = getCompressedOutputStream(tmp);
                is = new BufferedInputStream(new FileInputStream(f));
                byte[] buff = new byte[getCompressionBufferSize()];
                int read;
                do {
                    read = is.read(buff);
                    if (read > 0) os.write(buff, 0, read);
                } while (read > 0);
            } catch (Throwable ex) {
                logDebugEx("error compressing file " + f, ex);
            } finally {
                try {
                    if (is != null) is.close();
                    if (os != null) closeCompressedOutputStream(os);
                    if (f != null) {
                        f.delete();
                        if (tmp != null) tmp.renameTo(f);
                    }
                } catch (Throwable ex) {
                    logDebugEx("error closing files", ex);
                }
            }
        }
