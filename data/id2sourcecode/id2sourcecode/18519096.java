    public MediaElements downloadCannedMessage(int index) {
        if (cannedMessageList == null) {
            return null;
        }
        CannedElement element = (CannedElement) cannedMessageList.elementAt(index);
        if (element.getMessage() != null) {
            return element.getMessage();
        }
        String url_string = applet.getParameter("canned-message");
        if (url_string == null) {
            return null;
        }
        URL url = URLUtils.formatURL(applet, url_string);
        StringBuffer buffer = new StringBuffer(url.toString() + "?action=query");
        buffer.append("&group=" + URLEncoder.encode(element.getGroup()));
        buffer.append("&id=" + URLEncoder.encode(element.getId()));
        try {
            url = new URL(buffer.toString());
            URLConnection c = url.openConnection();
            c.setUseCaches(false);
            StringBuffer output = new StringBuffer();
            byte[] array = new byte[1000];
            InputStream is = c.getInputStream();
            int count = 0;
            while ((count = is.read(array)) > 0) {
                output.append(new String(array, 0, count));
            }
            CannedMessageParser cp = new CannedMessageParser();
            return cp.parseQueryMessage(output.toString());
        } catch (Exception ex) {
            return null;
        }
    }
