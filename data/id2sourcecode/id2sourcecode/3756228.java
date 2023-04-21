        private FileChannel getFileChannel(byte[] data) throws IOException {
            File file = File.createTempFile("temp", "temp");
            file.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            FileChannel fc = new RandomAccessFile(file, "r").getChannel();
            return fc;
        }
