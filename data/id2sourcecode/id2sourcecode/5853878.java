    private static IncludeFile retrieveIncludeFile(String path, String filename) {
        IncludeFile ret = null;
        File f = new File(new StringBuilder().append(path).append(Constants.fileSeparator).append(filename).toString());
        try {
            ret = new IncludeFile(new BufferedInputStream(new FileInputStream(f)), f.getParent());
        } catch (FileNotFoundException e) {
            try {
                String urlStr = new StringBuilder().append(path).append(filename).toString();
                URL url = null;
                if (urlStr.startsWith("jap:") || urlStr.startsWith("jar:")) {
                    url = new URL(urlStr.substring(0, 4) + new URI(urlStr.substring(4)).normalize().toString());
                } else {
                    url = new URL(new URI(urlStr).normalize().toString());
                }
                ret = new IncludeFile(url.openStream(), path);
            } catch (MalformedURLException mue) {
            } catch (IOException ioe) {
            } catch (URISyntaxException use) {
            }
        }
        return ret;
    }
