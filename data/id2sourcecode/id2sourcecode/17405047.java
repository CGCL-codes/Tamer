    private static EncodingInfo[] loadEncodingInfo() {
        URL url = null;
        try {
            String urlString = null;
            InputStream is = null;
            try {
                urlString = System.getProperty(ENCODINGS_PROP, "");
            } catch (SecurityException e) {
            }
            if (urlString != null && urlString.length() > 0) {
                url = new URL(urlString);
                is = url.openStream();
            }
            if (is == null) {
                SecuritySupport ss = SecuritySupport.getInstance();
                is = ss.getResourceAsStream(ObjectFactory.findClassLoader(), ENCODINGS_FILE);
            }
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
                is.close();
            } else {
            }
            int totalEntries = props.size();
            int totalMimeNames = 0;
            Enumeration keys = props.keys();
            for (int i = 0; i < totalEntries; ++i) {
                String javaName = (String) keys.nextElement();
                String val = props.getProperty(javaName);
                totalMimeNames++;
                int pos = val.indexOf(' ');
                for (int j = 0; j < pos; ++j) if (val.charAt(j) == ',') totalMimeNames++;
            }
            EncodingInfo[] ret = new EncodingInfo[totalMimeNames];
            int j = 0;
            keys = props.keys();
            for (int i = 0; i < totalEntries; ++i) {
                String javaName = (String) keys.nextElement();
                String val = props.getProperty(javaName);
                int pos = val.indexOf(' ');
                String mimeName;
                if (pos < 0) {
                    mimeName = val;
                } else {
                    StringTokenizer st = new StringTokenizer(val.substring(0, pos), ",");
                    for (boolean first = true; st.hasMoreTokens(); first = false) {
                        mimeName = st.nextToken();
                        ret[j] = new EncodingInfo(mimeName, javaName);
                        _encodingTableKeyMime.put(mimeName.toUpperCase(), ret[j]);
                        if (first) _encodingTableKeyJava.put(javaName.toUpperCase(), ret[j]);
                        j++;
                    }
                }
            }
            return ret;
        } catch (java.net.MalformedURLException mue) {
            throw new com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException(mue);
        } catch (java.io.IOException ioe) {
            throw new com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException(ioe);
        }
    }
