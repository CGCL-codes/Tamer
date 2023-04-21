    public static String getHTTP(String surl) {
        try {
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(bos);
            int BUF_SIZE = 1024;
            byte[] znak = new byte[BUF_SIZE];
            int bytesRead = 0;
            while ((bytesRead = in.read(znak)) > -1) out.write(znak, 0, bytesRead);
            in.close();
            out.close();
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
