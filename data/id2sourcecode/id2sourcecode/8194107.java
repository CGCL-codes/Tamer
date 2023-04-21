        public FakeZipEntry(ZipEntry entry, ZipInputStream inp) throws IOException {
            super(entry.getName());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = inp.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            data = baos.toByteArray();
        }
