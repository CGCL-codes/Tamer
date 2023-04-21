    private static InputStream readTestSegment(BufferedInputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(READ_TEST_LIMIT);
        try {
            byte[] data = new byte[READ_TEST_LIMIT];
            in.mark(READ_TEST_LIMIT);
            try {
                int read = in.read(data);
                while (read > 0 && bout.size() < READ_TEST_LIMIT) {
                    bout.write(data, 0, read);
                    int diff = READ_TEST_LIMIT - bout.size();
                    if (diff > 0) read = in.read(data, 0, diff);
                }
            } finally {
                in.reset();
            }
            return new ByteArrayInputStream(bout.toByteArray());
        } finally {
            bout.close();
        }
    }
