        private void copy(InputStream in, File destination) throws IOException {
            int read;
            byte[] block = new byte[blocksize];
            FileOutputStream writer = new FileOutputStream(destination);
            while ((read = in.read(block)) != -1) {
                writer.write(block, 0, read);
            }
            in.close();
            writer.flush();
            writer.close();
        }
