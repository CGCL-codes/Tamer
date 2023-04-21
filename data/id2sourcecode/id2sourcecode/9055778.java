    protected String getHtml() {
        try {
            URL url = new URL("http://www.youtube.com/watch?v=" + videoId);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            StringBuffer sb = new StringBuffer();
            byte[] buffer = new byte[512];
            int r = bis.read(buffer);
            while (r != -1) {
                sb.append(new String(buffer));
                r = bis.read(buffer);
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }
