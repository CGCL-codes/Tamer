    public static OutputStream getOutputStream(Result result) throws IOException {
        OutputStream out = null;
        if (result instanceof StreamResult) {
            out = ((StreamResult) result).getOutputStream();
        }
        if (out == null) {
            Writer w = ((StreamResult) result).getWriter();
            if (w != null) out = new WriterOutputStream(w);
        }
        if (out == null) {
            String systemId = result.getSystemId();
            if (systemId == null) {
                throw new IOException("no system ID");
            }
            try {
                URL url = new URL(systemId);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                out = connection.getOutputStream();
            } catch (MalformedURLException e) {
                out = new FileOutputStream(systemId);
            }
        }
        return out;
    }
