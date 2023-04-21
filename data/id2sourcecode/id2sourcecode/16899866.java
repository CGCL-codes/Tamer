        private Reader getUrlContents(String urlStr) throws Exception {
            URL urlObj = UrlExpandableList2.buildUrl(masterDocLocation, urlStr);
            URLConnection uConn = urlObj.openConnection();
            InputStream inStr = new BufferedInputStream(uConn.getInputStream());
            return new InputStreamReader(inStr);
        }
