    public static String getXMLString(URL url) {
        try {
            InputStream in = url.openStream();
            StringBuilder xml = new StringBuilder();
            byte[] buffer = new byte[512];
            int readen = 0;
            while ((readen = in.read(buffer)) != -1) {
                xml.append(new String(buffer, 0, readen));
            }
            String doc = xml.toString();
            if (doc.indexOf(buggyChar) != -1) {
                doc = doc.replace(buggyChar, ' ');
            }
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
