    public static String download(String address) throws Exception {
        URLConnection conn = null;
        InputStream in = null;
        String R = "";
        try {
            URL url = new URL(address);
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                R += new String(buffer, 0, numRead);
            }
        } catch (MalformedURLException ex) {
            Utils.showException(ex);
            throw ex;
        } catch (IOException ex) {
            Utils.showException(ex);
            throw ex;
        }
        return R;
    }
