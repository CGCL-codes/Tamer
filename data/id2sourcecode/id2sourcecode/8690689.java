    public void issue(String licenseURI) throws IOException {
        String issueUrl = cc_root + "/details?license-uri=" + licenseURI;
        String post_data;
        try {
            post_data = URLEncoder.encode("license-uri", "UTF-8") + "=" + URLEncoder.encode(licenseURI, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return;
        }
        URL request_url;
        try {
            request_url = new URL(issueUrl);
        } catch (MalformedURLException e) {
            return;
        }
        URLConnection connection = request_url.openConnection();
        connection.setDoOutput(true);
        try {
            java.io.InputStream stream = connection.getInputStream();
            license_doc = this.parser.build(stream);
        } catch (JDOMException jde) {
            log.warn(jde.getMessage());
        } catch (Exception e) {
            log.warn(e.getCause());
        }
        return;
    }
