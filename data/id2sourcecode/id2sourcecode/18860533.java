    public void copy(String from, String to) throws IOException {
        FileConnection origConn = getFileConnection(from, Connector.READ);
        try {
            FileConnection destConn = getFileConnection(to, Connector.WRITE);
            try {
                if (destConn.exists()) {
                    throw new IOException("File exists");
                }
                InputStream is = origConn.openInputStream();
                try {
                    OutputStream os = destConn.openOutputStream();
                    byte[] buf = new byte[512];
                    while (is.available() > 0) {
                        int read = is.read(buf, 0, Math.min(is.available(), buf.length));
                        os.write(buf, 0, read);
                    }
                } finally {
                    is.close();
                }
            } finally {
                destConn.close();
            }
        } finally {
            origConn.close();
        }
    }
