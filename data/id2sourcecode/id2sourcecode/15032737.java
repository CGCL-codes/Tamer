    private void addBookmark(String url) {
        if (!Cast.URL.isA(url)) {
            cout().print("No a URL: " + url);
            return;
        }
        try {
            final String suffix = "/in/photostream/";
            if (url.endsWith(suffix)) {
                url = url.substring(0, url.length() - suffix.length());
            }
            String title = null;
            String src = null;
            InputStream io = openStream(url);
            String html = IOUtils.getReaderContent(new InputStreamReader(io));
            io.close();
            int i = -1;
            while ((i = html.indexOf("<img", i + 1)) != -1) {
                i += 3;
                title = null;
                src = null;
                int j = html.indexOf(">", i);
                if (j == -1) continue;
                String tag = html.substring(i, j);
                String tokens[] = tag.split("[ \t\"\'\n=]+");
                for (int k = 0; k + 1 < tokens.length; ++k) {
                    if (tokens[k].equals("src") && tokens[k + 1].startsWith("http://farm") && tokens[k + 1].contains(".static.flickr.com/")) {
                        src = tokens[k + 1];
                        int m = src.indexOf('?');
                        if (m != -1) src = src.substring(0, m);
                    } else if (tokens[k].equals("alt")) {
                        title = tokens[k + 1];
                    }
                }
                if (title != null && src != null) {
                    Resource subject = getModel().createResource(url);
                    cout().printf(title);
                    if (getModel().containsResource(subject)) {
                        cout().println("Already in Model");
                        continue;
                    }
                    getModel().add(subject, RDF.type, isBookmark).add(subject, DC.title, title).add(subject, DC.date, this.dateFormat.format(new Date())).add(subject, thumb, getModel().createResource(src)).add(subject, image, getModel().createResource(src));
                    return;
                }
            }
            cout().println("Failure");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
