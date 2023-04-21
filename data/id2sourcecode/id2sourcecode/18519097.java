    private void downloadCannedMessageList(GroupList list) {
        if (list == null) {
            return;
        }
        int num_groups = list.numElements();
        if (num_groups == 0) {
            return;
        }
        String url_string = applet.getParameter("canned-message");
        if (url_string == null) {
            return;
        }
        statusBar.setText(ResourceBundle.getBundle("com.quikj.application.web.talk.client.language", locale).getString("Downloading_pre-defined_messages") + "...");
        URL url = URLUtils.formatURL(applet, url_string);
        StringBuffer buffer = new StringBuffer(url.toString() + "?action=list&groups=all");
        for (int i = 0; i < num_groups; i++) {
            buffer.append(URLEncoder.encode("," + list.getElementAt(i)));
        }
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
            cannedMessageList = cp.parseListMessage(output.toString());
        } catch (Exception ex) {
            return;
        }
    }
