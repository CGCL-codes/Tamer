    public HttpFeed(String name, String url) {
        String inputFile = escapeFilename(name);
        try {
            File folder = new File(Feed2ereader.tempFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String cacheFile = "./news/" + inputFile + ".xml";
            System.out.println("Downloading \"" + name + "\" at " + url + "...");
            if (HttpGet.cache(cacheFile, url)) {
                FileFeed fileFeed = new FileFeed(cacheFile);
                this.channel = fileFeed.getChannel();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
