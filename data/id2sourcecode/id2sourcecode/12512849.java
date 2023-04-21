    public void process() {
        URL urlObj = null;
        URLConnection conn = null;
        InputStream istream = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            urlObj = new URL(urlSite);
            conn = urlObj.openConnection();
            istream = conn.getInputStream();
        } catch (Exception e) {
            Debug.inform("Unable to retrieve URL '" + urlSite + "': " + e.getMessage());
            return;
        }
        byte dat[];
        dat = new byte[32768];
        while (true) {
            int bytesRead;
            try {
                bytesRead = istream.read(dat);
                bos.write(dat, 0, bytesRead);
            } catch (Exception e) {
                Debug.inform("Unable to read data from URL Stream: " + e.getMessage());
                break;
            }
            if (bytesRead <= 0) {
                break;
            }
        }
        String retstr = bos.toString();
        try {
            istream.close();
        } catch (Exception e) {
            Debug.inform("Unable to close input stream: " + e.getMessage());
        }
        Debug.inform("Request return='" + retstr + "'");
        setControllerInactive();
    }
