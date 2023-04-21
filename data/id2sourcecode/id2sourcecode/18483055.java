        private static ByteArrayInputStream copy(InputStream is) throws IOException {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = is.read(buf, 0, 1024)) > 0) baos.write(buf, 0, bytesRead);
            return new ByteArrayInputStream(baos.toByteArray());
        }
