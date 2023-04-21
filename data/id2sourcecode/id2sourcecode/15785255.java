    private String doConvey(URL url, String msg) throws IOException {
        BufferedReader rd = null;
        OutputStreamWriter wr = null;
        URLConnection uc = null;
        try {
            if (url == null) {
                return "";
            }
            logger.debug("destination url : " + url);
            logger.debug("conveying request  : " + msg);
            uc = url.openConnection();
            setupHostNameVerifier(url, uc);
            setupTimeouts(uc);
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            uc.setRequestProperty("Content-Type", contentType);
            wr = new OutputStreamWriter(uc.getOutputStream());
            wr.write(msg.toString());
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuffer res = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                logger.debug("response : " + line);
                if (res.length() > 0) {
                    res.append("\r\n");
                }
                res.append(line);
            }
            wr.close();
            rd.close();
            return res.toString();
        } catch (IOException e) {
            logger.error("failed to convey message to " + url + " : " + e.getMessage());
            if (wr != null) {
                wr.close();
            }
            if (rd != null) {
                rd.close();
            }
            if (uc instanceof HttpURLConnection) {
                ((HttpURLConnection) uc).disconnect();
            }
            throw e;
        }
    }
