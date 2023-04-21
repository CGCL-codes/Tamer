    public Date getDate() throws Exception {
        if (Utilities.isType(m_res, Constants.s_content_LiteralContent, m_source)) {
            return null;
        } else if (Utilities.isType(m_res, Constants.s_content_JavaClasspathContent, m_source)) {
            return null;
        } else if (Utilities.isType(m_res, Constants.s_content_FilesystemContent, m_source) || (m_res.getURI().toLowerCase().indexOf("file://") == 0)) {
            URL url = new URL(m_res.getURI());
            if (!InetAddress.getLocalHost().getHostAddress().equals(InetAddress.getByName(url.getHost()).getHostAddress())) {
                throw new UnsupportedOperationException("Connecting to external hosts not supported: " + url.getHost());
            }
            String str = url.getPath();
            if ((str.indexOf(':') != -1) && (str.indexOf('/') == 0)) {
                str = str.substring(1);
            }
            return new Date(new File(str).lastModified());
        } else {
            URL url = new URL(m_res.getURI());
            HttpURLConnection urlc = (HttpURLConnection) (url.openConnection());
            urlc.setRequestMethod("HEAD");
            urlc.connect();
            return new Date(urlc.getLastModified());
        }
    }
