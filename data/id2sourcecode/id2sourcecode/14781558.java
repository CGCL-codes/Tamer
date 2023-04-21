    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase(CATEGORY_TAG)) {
            String name = attributes.getValue(NAME_ATTR);
            _currentCategory = getCategory(name);
        } else if (qName.equalsIgnoreCase(SOURCE_TAG)) {
            String name = attributes.getValue(NAME_ATTR);
            String src = attributes.getValue(SRC_ATTR);
            String site = attributes.getValue(SITE_ATTR);
            String username = attributes.getValue(USERNAME_ATTR);
            String password = attributes.getValue(PASSWORD_ATTR);
            NewsSource source = new NewsSource(_currentId++, name, src);
            if (username != null && username.length() > 0 && password != null && password.length() > 0) {
                source.setUsername(username);
                source.setPassword(password);
            }
            String refreshStr = attributes.getValue(REFRESH_ATTR);
            if (refreshStr != null) {
                try {
                    long refresh = Integer.parseInt(refreshStr) * 60 * 1000L;
                    source.setRefresh(refresh);
                } catch (NumberFormatException ex) {
                    Log.getInstance().write("Problem reading refresh frequency" + " for news source: " + name, ex);
                }
            }
            if (site == null) {
                try {
                    URL srcURL = new URL(src);
                    site = srcURL.getProtocol() + "://" + srcURL.getHost();
                } catch (MalformedURLException ex) {
                    Log.getInstance().write("Problem construcing src url: " + src, ex);
                }
            }
            source.setSiteURL(site);
            _currentCategory.addNewsSource(source);
        }
    }
