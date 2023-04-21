    protected void setMediaProps(String mediaURLstring) {
        if (mediaURLstring != null) {
            URL url = null;
            try {
                url = new URL(mediaURLstring);
            } catch (MalformedURLException mue1) {
                try {
                    url = new URL("file", "", CURRENTDIR + mediaURLstring);
                } catch (MalformedURLException mue2) {
                    writeLog("Unable to find the media properties file based on parameter 'mediaURL' = " + mediaURLstring, HttpServletResponse.SC_ACCEPTED, mue2);
                    url = null;
                }
            }
            if (url != null) {
                try {
                    ourMediaProps = new OrderedProps(url.openStream());
                } catch (IOException ioe1) {
                    writeLog("Exception occurred while opening media properties file: " + mediaURLstring + ".  Media table may be invalid.", HttpServletResponse.SC_ACCEPTED, ioe1);
                }
            }
        } else {
            String defaultProp = CURRENTDIR + "media.properties";
            try {
                ourMediaProps = new OrderedProps(new FileInputStream(defaultProp));
            } catch (IOException ioe2) {
                writeLog("Default media properties file " + defaultProp + " not found.", HttpServletResponse.SC_ACCEPTED, ioe2);
            }
        }
    }
