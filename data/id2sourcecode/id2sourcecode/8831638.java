    public String getPage(java.net.URL url) {
        StringBuffer html = new StringBuffer();
        String cr = "\n";
        try {
            String line = null;
            java.io.BufferedReader dis = new java.io.BufferedReader(new java.io.InputStreamReader(url.openConnection().getInputStream()));
            while ((line = dis.readLine()) != null) {
                html.append(line);
                html.append(cr);
            }
        } catch (Exception e) {
            html.append("An error occurred:\n" + e.toString());
        }
        return html.toString();
    }
