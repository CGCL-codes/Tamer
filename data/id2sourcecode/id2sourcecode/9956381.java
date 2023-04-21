    public static byte[] download(String address, ConnectionContext ccontext) throws IOException {
        ByteArrayOutputStream out = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        String token = ccontext.getAuthToken();
        try {
            URL url = new URL(address);
            out = new ByteArrayOutputStream();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "ZM_AUTH_TOKEN=" + token);
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
            return out.toByteArray();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                FunambolLogger logger = FunambolLoggerFactory.getLogger("funambol.zimbra.manager");
                logger.error("Downloading from Zimbra went wrong", ioe);
            }
        }
    }
