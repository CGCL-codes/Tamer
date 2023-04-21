    public InetAddress ping(String id, String action) throws Exception {
        InetAddress res = null;
        InputStream in = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[100];
        int len;
        try {
            URL url = new URL("http://" + addr.getHostName() + "/cgi-bin/pong.pl?pfx=" + id + "&a=" + action);
            in = url.openStream();
            while ((len = in.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            baos.close();
            in.close();
            String sByt = new String(baos.toByteArray(), "iso-8859-1");
            Pattern p = Pattern.compile(".*?([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+).*");
            Matcher m = p.matcher(sByt);
            if (m.matches()) {
                byte[] bIP = new byte[4];
                for (int i = 0; i < 4; i++) {
                    bIP[i] = (byte) Integer.parseInt(m.group(i + 1));
                }
                res = InetAddress.getByAddress(bIP);
            }
        } catch (Exception ex) {
            ExHandler.handle(ex);
        }
        return res;
    }
