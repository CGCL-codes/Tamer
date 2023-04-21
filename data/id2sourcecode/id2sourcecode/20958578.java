    private boolean fetchCover(Tag tag) {
        if (tag.getTagName().equalsIgnoreCase("div") && tag.getAttribute("id") != null && tag.getAttribute("id").equals("tn15lhs")) {
            Node divPhoto = tag.getChildren().elementAt(1);
            if ((divPhoto instanceof Div) == false) return false;
            LinkTag linkTag = (LinkTag) divPhoto.getChildren().elementAt(1);
            final String href = linkTag.getAttribute("href");
            LOG.debug("Fetching cover from href '" + href + "'.");
            if (href.startsWith("/media/") == false) {
                LOG.info("Found href '" + href + "' does not start with /media/; skipping it.");
                return false;
            }
            final String url = ImdbWebDataFetcher.HOST + href;
            URLConnection connection;
            try {
                connection = ProxyEnabledConnectionFactory.openConnection(new URL(url));
            } catch (MalformedURLException e) {
                throw new FatalException("Malformed url given '" + url + "'!", e);
            } catch (IOException e) {
                throw new FatalException("Could not connect to website by url '" + url + "'!", e);
            }
            try {
                Parser parser = new Parser(connection);
                final ImdbCoverFetcher visitor = new ImdbCoverFetcher();
                parser.visitAllNodesWith(visitor);
                if (visitor.isCoverDownloaded() == true) {
                    final String newCoverFile = visitor.getDownloadedFile().getAbsolutePath();
                    LOG.debug("Setting coverfile form fetched image to '" + newCoverFile + "'.");
                    this.movieData.setCoverFile(newCoverFile);
                }
            } catch (ParserException e) {
                throw new FatalException("Could not fetch cover for url '" + url + "'!", e);
            }
            return true;
        }
        return false;
    }
