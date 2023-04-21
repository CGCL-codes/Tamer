    public static String getIP() {
        String sUrl = "http://www.paecker.net/fileadmin/getip.php";
        StringBuffer sb = new StringBuffer(1024);
        try {
            URL url = new URL(sUrl);
            InputStream is = url.openStream();
            DataInputStream data = new DataInputStream(new BufferedInputStream(is));
            String line;
            while ((line = data.readLine()) != null) sb.append(line);
        } catch (Exception e) {
        }
        return sb.toString();
    }
