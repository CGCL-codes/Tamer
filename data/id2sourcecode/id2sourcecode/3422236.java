    String readLicense() {
        URL url;
        url = getClass().getResource("/COPYING");
        if (url == null) return null;
        StringBuffer text = new StringBuffer();
        try {
            InputStream in = url.openStream();
            BufferedReader ir = new BufferedReader(new InputStreamReader(in));
            while (ir.ready()) {
                String line = ir.readLine();
                text.append(line);
                text.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }
