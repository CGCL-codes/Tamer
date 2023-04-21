    public String fetchNewPatientIDfromNIST() throws IOException {
        URL url = new URL(fetchNewPatIDURL);
        configProxyAndTLS(url.getProtocol());
        InputStream is = url.openStream();
        byte[] buffer = new byte[4096];
        int len;
        StringBuffer sb = new StringBuffer();
        while ((len = is.read(buffer)) > 0) {
            sb.append(new String(buffer, 0, len));
        }
        log.debug("response of fetching new patientID:" + sb);
        String response = sb.toString().trim();
        int pos = response.indexOf("ISO");
        response = response.substring(0, pos + 3);
        pos = response.lastIndexOf(">");
        if (pos == -1) pos = response.lastIndexOf("=");
        response = response.substring(pos + 1);
        return response;
    }
