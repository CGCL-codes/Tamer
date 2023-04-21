    public boolean parse(URL url) throws IOException {
        if (hasParsedFile(new File(url.getFile()))) {
            return true;
        }
        return parse(url.toString(), url.openStream());
    }
