    public String getGeographicalPosition(String ip) {
        String address = null;
        byte b[] = new byte[100];
        int n = -1;
        if (ip.equals("0.0.0.0") || ip.equals("127.0.0.1")) return "���ؼ����";
        try {
            URL url = new URL("http://www.gopawpaw.com/tool/getGeographicalPosition.php?actionip=" + ip);
            InputStream in = url.openStream();
            while ((n = in.read(b)) != -1) {
                address = new String(b, 0, n);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
