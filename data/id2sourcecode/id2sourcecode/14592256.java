    public URLConnection openConnection(URL url) throws IOException {
        String urlFile = url.getFile();
        int barIndex = urlFile.indexOf("|");
        if (barIndex == -1) throw new MalformedURLException("Missing '|'");
        String fileName = urlFile.substring(0, barIndex);
        String entryName = urlFile.substring(barIndex + 1);
        return new ZipURLConnection(url, new File(fileName), entryName);
    }
