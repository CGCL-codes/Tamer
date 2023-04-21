        private byte[] getFileBytes(File f) throws IOException {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f), 2048);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            int b;
            while ((b = bis.read()) != -1) baos.write(b);
            bis.close();
            return baos.toByteArray();
        }
