    public ComixPage buildPage() {
        ComixPage page = new ComixPage(url);
        try {
            InputStream pageStream = (InputStream) url.getContent();
            String pageContent = streamToString(pageStream);
            pageStream.close();
            if (imagePattern != null) {
                String value = find(imagePattern, pageContent);
                page.setImageUrl(new URL(url, value));
            }
            if (nextPagePattern != null) {
                String value = find(nextPagePattern, pageContent);
                page.setNextPageUrl(new URL(url, value));
            }
            if (titlePattern != null) {
                String value = find(titlePattern, pageContent);
                page.setTitle(value);
            }
            if (descriptionPattern != null) {
                String value = find(descriptionPattern, pageContent);
                page.setDescription(value);
            }
            if (cacheLocation != null) {
                InputStream input = null;
                OutputStream output = null;
                try {
                    URL cacheUrl = new URL(cacheLocation.toExternalForm() + "/" + page.getFileName());
                    URLConnection urlConnection = page.getImageUrl().openConnection();
                    File file = new File(cacheUrl.toURI());
                    urlConnection.connect();
                    input = new DataInputStream(urlConnection.getInputStream());
                    output = new FileOutputStream(file);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = input.read(buf)) > 0) {
                        output.write(buf, 0, len);
                    }
                    page.setCachedImageUrl(cacheUrl);
                } catch (IOException e) {
                    logger.error("Could not cache image for page " + url, e);
                } catch (URISyntaxException e) {
                    logger.error("Could not cache image for page " + url, e);
                } finally {
                    closeStream(input);
                    closeStream(output);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return page;
    }
