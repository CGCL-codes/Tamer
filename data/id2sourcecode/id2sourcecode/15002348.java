    public void setHtml(WebBrowser browser) {
        String htmlCode = "";
        try {
            URL url = browser.getURL();
            URLConnection conn = url.openConnection();
            String contentType = conn.getContentType();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), getContentEncoding(contentType)));
            String buffer = "";
            while (buffer != null) {
                try {
                    htmlCode += buffer + "\n";
                    buffer = br.readLine();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    break;
                }
            }
            this.html = htmlCode;
        } catch (Exception e) {
        }
    }
