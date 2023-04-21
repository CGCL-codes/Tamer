    public static int POSTtheSGF(String basePath, String sgf) {
        URL url;
        URLConnection urlConn;
        DataOutputStream printout;
        try {
            url = new URL(basePath + "util/addlobj.html");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes("lobj=" + sgf);
            printout.flush();
            printout.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            String res = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                res = line;
            }
            in.close();
            if (res.length() > 0) {
                try {
                    int ret = Integer.parseInt(res);
                    return ret;
                } catch (NumberFormatException ne) {
                }
            } else System.out.println("error on POST: no data returned");
        } catch (java.io.IOException ie) {
            System.out.println("error on POST: " + ie);
        }
        return 0;
    }
