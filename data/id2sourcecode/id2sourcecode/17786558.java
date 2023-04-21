        public OutputStream newEntry(String name) throws IOException {
            zout.putNextEntry(new ZipEntry(name));
            return zout;
        }
