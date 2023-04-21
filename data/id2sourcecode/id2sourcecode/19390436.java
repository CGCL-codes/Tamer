    public void run() {
        try {
            java.net.URL url = new java.net.URL(this.getUrl());
            java.net.URLConnection urlconn = url.openConnection();
            urlconn.setDoInput(true);
            java.io.InputStream is = urlconn.getInputStream();
            org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sb.build(is);
            this.setText((new org.jdom.output.XMLOutputter()).outputString(doc));
        } catch (Exception exp) {
        }
    }
