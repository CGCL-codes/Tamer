    private void writeTextTo(Reader reader, PrintWriter writer) throws IOException {
        char[] buff = new char[8192];
        try {
            if (reader == null || writer == null) throw new IOException("Stream is null. reader=" + reader + ", writer = " + writer);
            int len = reader.read(buff, 0, buff.length);
            while (len != -1) {
                writer.write(buff, 0, len);
                len = reader.read(buff, 0, buff.length);
            }
            writer.flush();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
