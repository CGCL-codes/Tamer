    public String embed(URL url, String name) throws EmailException {
        if (StringUtils.isEmpty(name)) {
            throw new EmailException("name cannot be null or empty");
        }
        if (inlineEmbeds.containsKey(name)) {
            InlineImage ii = (InlineImage) inlineEmbeds.get(name);
            URLDataSource urlDataSource = (URLDataSource) ii.getDataSource();
            if (url.toExternalForm().equals(urlDataSource.getURL().toExternalForm())) {
                return ii.getCid();
            } else {
                throw new EmailException("embedded name '" + name + "' is already bound to URL " + urlDataSource.getURL() + "; existing names cannot be rebound");
            }
        }
        InputStream is = null;
        try {
            is = url.openStream();
        } catch (IOException e) {
            throw new EmailException("Invalid URL", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }
        }
        return embed(new URLDataSource(url), name);
    }
