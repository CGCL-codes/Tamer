    public static boolean post(String[] params, byte[][] values, String[] contentTypes, String surl, StringBuffer ret, ApplicationParams applet) {
        try {
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty(HTTP_REQUEST_CONTENT_TYPE, "multipart/form-data; boundary=" + HTTP_BOUNDARY);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            HelperContentPackage.setStdRequestHeadersForPlatformServer(con, applet);
            byte[] data = getPOSTContent(params, values, contentTypes);
            OutputStream out = con.getOutputStream();
            System.out.println("sending content:" + data.length);
            out.write(data);
            out.flush();
            out.close();
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("line: " + line);
                ret.append(line);
            }
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            ret.append("\n");
            ret.append(t.getMessage());
        }
        return false;
    }
