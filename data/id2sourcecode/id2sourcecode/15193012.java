        public Object getConnection() {
            String urlStr = storeMgr.getConnectionURL();
            urlStr = urlStr.substring(urlStr.indexOf(storeMgr.getStoreManagerKey() + ":") + storeMgr.getStoreManagerKey().length() + 1);
            if (options.containsKey(STORE_JSON_URL)) {
                if (urlStr.endsWith("/") && options.get(STORE_JSON_URL).toString().startsWith("/")) {
                    urlStr += options.get(STORE_JSON_URL).toString().substring(1);
                } else if (!urlStr.endsWith("/") && !options.get(STORE_JSON_URL).toString().startsWith("/")) {
                    urlStr += "/" + options.get(STORE_JSON_URL).toString();
                } else {
                    urlStr += options.get(STORE_JSON_URL).toString();
                }
            }
            URL url;
            try {
                url = new URL(urlStr);
                return url.openConnection();
            } catch (MalformedURLException e) {
                throw new NucleusDataStoreException(e.getMessage(), e);
            } catch (IOException e) {
                throw new NucleusDataStoreException(e.getMessage(), e);
            }
        }
