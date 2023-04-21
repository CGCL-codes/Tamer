    public void issue(String licenseId, Map answers, String lang) throws IOException {
        String issueUrl = this.cc_root + "/license/" + licenseId + "/issue";
        String answer_doc = "<answers>\n<locale>" + lang + "</locale>\n" + "<license-" + licenseId + ">\n";
        Iterator keys = answers.keySet().iterator();
        try {
            String current = (String) keys.next();
            while (true) {
                answer_doc += "<" + current + ">" + (String) answers.get(current) + "</" + current + ">\n";
                current = (String) keys.next();
            }
        } catch (NoSuchElementException e) {
        }
        answer_doc += "</license-" + licenseId + ">\n</answers>\n";
        String post_data;
        try {
            post_data = URLEncoder.encode("answers", "UTF-8") + "=" + URLEncoder.encode(answer_doc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return;
        }
        URL post_url;
        try {
            post_url = new URL(issueUrl);
        } catch (MalformedURLException e) {
            return;
        }
        URLConnection connection = post_url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(post_data);
        writer.flush();
        try {
            java.io.InputStream stream = connection.getInputStream();
            this.license_doc = this.parser.build(stream);
        } catch (JDOMException jde) {
            log.warn(jde.getMessage());
        } catch (Exception e) {
            log.warn(e.getCause());
        }
        return;
    }
