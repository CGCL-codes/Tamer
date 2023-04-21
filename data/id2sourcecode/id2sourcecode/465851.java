    private String readURLContent(URL url) {
        String output = "";
        try {
            InputStream in = url.openStream();
            byte[] buffer = new byte[102400];
            int numRead;
            int fileSize = 0;
            while ((numRead = in.read(buffer)) != -1) {
                output += new String(buffer, 0, numRead);
                fileSize += numRead;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return output;
    }
