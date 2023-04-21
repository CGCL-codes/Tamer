    public String readCharacterStream(InputStream stream) throws IOException {
        if (stream == null) throw new IllegalArgumentException("supplied stream was null");
        try {
            StringWriter sw = new StringWriter();
            int n;
            while ((n = stream.read()) >= 0) sw.write(n);
            return sw.toString();
        } finally {
            if (stream != null) stream.close();
        }
    }
