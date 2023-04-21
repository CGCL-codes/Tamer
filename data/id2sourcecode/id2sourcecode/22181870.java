    public ConcreteElement getContent(RunData rundata) {
        String servletURL = processURL(rundata);
        if (servletURL == null) {
            return new StringElement("ServletInvokerPortlet:  Must specify a URL using the URL parameter");
        }
        String content;
        try {
            URL url = new URL(servletURL);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(stream);
            int length = 0;
            byte[] buf = new byte[BUFFER_SIZE];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((in != null) && ((length = in.read(buf)) != -1)) {
                out.write(buf, 0, length);
            }
            content = out.toString();
            return new StringElement(content);
        } catch (Exception e) {
            String message = "ServletInvokerPortlet: Error invoking " + servletURL + ": " + e.getMessage();
            return new StringElement(message);
        }
    }
