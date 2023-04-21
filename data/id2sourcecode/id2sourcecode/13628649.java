        public void saveToFile(File file) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] block = new byte[256];
                int length;
                while ((length = is.read(block)) != -1) fos.write(block, 0, length);
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
