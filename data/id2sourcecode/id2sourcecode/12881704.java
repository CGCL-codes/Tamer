    private void copy(InputStream iStr, OutputStream oStr) throws IOException {
        try {
            while (iStr.available() > 0) {
                oStr.write(iStr.read());
            }
        } finally {
            if (oStr != null) {
                oStr.flush();
                oStr.close();
            }
            if (iStr != null) {
                iStr.close();
            }
        }
    }
