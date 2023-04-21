    public static StringBuilder getContent(final URL url) throws IOException {
        final StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
            final char[] readBuffer = new char[BUFFER_SIZE];
            int numRead = 0;
            do {
                int offset = 0;
                while (BUFFER_SIZE > offset && 0 <= (numRead = reader.read(readBuffer, offset, BUFFER_SIZE - offset))) offset += numRead;
                buffer.append(readBuffer, 0, offset);
            } while (0 <= numRead);
        } finally {
            if (reader != null) reader.close();
        }
        buffer.trimToSize();
        return buffer;
    }
