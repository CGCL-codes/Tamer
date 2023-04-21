        @Override
        protected void preCompress(File in, ZipOutputStream deflaterStream) throws IOException {
            ZipEntry entry = new ZipEntry(in.getName());
            entry.setTime(in.lastModified());
            deflaterStream.putNextEntry(entry);
        }
