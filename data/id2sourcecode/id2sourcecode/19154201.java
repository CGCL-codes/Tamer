    public static String getContentAsString(String fileUrl) throws java.io.FileNotFoundException, java.io.IOException {
        try {
            File fileContent = new File(fileUrl);
            if (fileContent != null) {
                java.net.URL url = fileContent.toURI().toURL();
                java.io.InputStream inputStream = url.openStream();
                if (inputStream != null) {
                    StringBuffer buf = null;
                    int i = -1;
                    while ((i = inputStream.read()) != -1) {
                        if (buf == null) {
                            buf = new StringBuffer();
                        }
                        buf.append((char) i);
                    }
                    inputStream.close();
                    if (buf != null) {
                        return buf.toString();
                    }
                }
            }
        } catch (java.io.FileNotFoundException e) {
            StringBuffer buf = new StringBuffer();
            buf.append("Url [");
            buf.append(fileUrl);
            buf.append("] is not a valid file. Verify the document setup.");
            throw new java.io.FileNotFoundException(buf.toString());
        } catch (java.io.IOException e) {
            StringBuffer buf = new StringBuffer();
            buf.append("Unable to retrieve content from url [");
            buf.append(fileUrl);
            buf.append("]. Verify the document setup.");
            throw new java.io.IOException(buf.toString());
        }
        return null;
    }
