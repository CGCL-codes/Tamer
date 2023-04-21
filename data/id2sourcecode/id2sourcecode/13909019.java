    private boolean processURL(URL url, String baseDir, StatusWindow status) throws IOException {
        if (processedLinks.contains(url)) {
            return false;
        } else {
            processedLinks.add(url);
        }
        URLConnection connection = url.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        ArrayList<String> list = processPage(in, baseDir, url);
        if ((status != null) && (list.size() > 0)) {
            status.setMaximum(list.size());
        }
        for (int i = 0; i < list.size(); i++) {
            if (status != null) {
                status.setMessage(Utils.trimFileName(list.get(i).toString(), 40), i);
            }
            if ((!((String) list.get(i)).startsWith("RUN")) && (!((String) list.get(i)).startsWith("SAVE")) && (!((String) list.get(i)).startsWith("LOAD"))) {
                processURL(new URL(url.getProtocol(), url.getHost(), url.getPort(), (String) list.get(i)), baseDir, status);
            }
        }
        in.close();
        return true;
    }
