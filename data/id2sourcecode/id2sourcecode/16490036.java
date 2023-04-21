    private URLConnection openConnection(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        URLConnection con = null;
        if (BaseHelpSystem.getMode() == BaseHelpSystem.MODE_INFOCENTER) {
            String locale = UrlUtil.getLocale(request, response);
            if (url.indexOf('?') >= 0) {
                url = url + "&lang=" + locale;
            } else {
                url = url + "?lang=" + locale;
            }
        }
        URL helpURL;
        if (url.startsWith("help:")) {
            helpURL = new URL("help", null, -1, url.substring("help:".length()), HelpURLStreamHandler.getDefault());
        } else {
            if (url.startsWith("jar:")) {
                int excl = url.indexOf("!/");
                String jar = url.substring(0, excl);
                String path = url.length() > excl + 2 ? url.substring(excl + 2) : "";
                url = jar.replaceAll("!", "%21") + "!/" + path.replaceAll("!", "%21");
            }
            helpURL = new URL(url);
        }
        String protocol = helpURL.getProtocol();
        if (!("help".equals(protocol) || "file".equals(protocol) || "platform".equals(protocol) || "jar".equals(protocol))) {
            throw new IOException();
        }
        con = helpURL.openConnection();
        con.setAllowUserInteraction(false);
        con.setDoInput(true);
        con.connect();
        return con;
    }
