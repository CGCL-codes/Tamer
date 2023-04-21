        public OutputStream openEntry(final String name) throws IOException {
            ZipEntry entry = new ZipEntry(name);
            zos.putNextEntry(entry);
            return zos;
        }
