    private Document setBlockFromURL(URL urlSource) {
        String strSource = "";
        try {
            URLConnection conn = urlSource.openConnection();
            InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            StringBuffer sb = new StringBuffer();
            int ch = reader.read();
            while (ch != -1) {
                sb.append((char) ch);
                ch = reader.read();
            }
            strSource = sb.toString();
            reader.close();
        } catch (IOException ex) {
            m_logger.error("setBlockFromURL(URL)", ex);
            docBlock = null;
            return null;
        }
        return setBlock(strSource);
    }
