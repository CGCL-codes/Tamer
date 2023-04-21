        public FileEntryInfo(InputStream in, long lastModified, File fromArchive, int mode) throws IOException {
            super(lastModified, mode);
            tmpFile = createTempFile("gwtjar", "");
            tmpFile.deleteOnExit();
            OutputStream fos = new FileOutputStream(tmpFile);
            int readLen = in.read(buffer);
            while (readLen > 0) {
                fos.write(buffer, 0, readLen);
                readLen = in.read(buffer);
            }
            fos.close();
            archive = fromArchive;
        }
