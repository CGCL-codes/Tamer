        private long getDataLength() throws IOException {
            long len;
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            byte b[] = new byte[1024];
            int read;
            while ((read = source.read(b)) >= 0) s.write(b, 0, read);
            source.close();
            b = s.toByteArray();
            len = b.length;
            source = new ByteArrayInputStream(b);
            return len;
        }
