    private void init(InputSource in) {
        try {
            transformedDoc = null;
            caw = new CharArrayWriter();
            Reader reader = in.getCharacterStream();
            caw = new CharArrayWriter();
            long total = 0;
            char[] buff = new char[1024];
            while (true) {
                int read = reader.read(buff, 0, buff.length);
                total += read;
                if (read <= 0) break;
                caw.write(buff, 0, read);
            }
            caw.close();
            reader.close();
        } catch (java.io.IOException iox) {
            logger.error(iox.getMessage(), iox);
        }
    }
