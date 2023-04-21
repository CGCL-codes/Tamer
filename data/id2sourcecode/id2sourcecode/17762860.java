        private ByteBuffer getReadOnlyMemoryMappedBuffer(File file) throws IOException {
            ByteBuffer bb = null;
            FileInputStream in = null;
            FileChannel c = null;
            assert file.exists() : "No file " + file.getAbsolutePath();
            try {
                in = new FileInputStream(file);
                c = in.getChannel();
                bb = c.map(FileChannel.MapMode.READ_ONLY, 0, c.size()).asReadOnlyBuffer();
            } finally {
                if (c != null && c.isOpen()) {
                    c.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            return bb;
        }
