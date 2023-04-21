        private static CharArrayReader copy(Reader is) throws IOException {
            char[] buf = new char[1024];
            CharArrayWriter baos = new CharArrayWriter();
            int bytesRead;
            while ((bytesRead = is.read(buf, 0, 1024)) > 0) baos.write(buf, 0, bytesRead);
            return new CharArrayReader(baos.toCharArray());
        }
