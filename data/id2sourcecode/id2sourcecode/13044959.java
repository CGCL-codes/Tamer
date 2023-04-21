        private byte[] getClassBytes(String slashedName) {
            URL url = Thread.currentThread().getContextClassLoader().getResource(slashedName + ".class");
            if (url == null) {
                logger.log(TreeLogger.DEBUG, "Unable to find " + slashedName + " on the classPath");
                return null;
            }
            String urlStr = url.toExternalForm();
            if (slashedName.equals(mainClass)) {
                mainUrlBase = urlStr.substring(0, urlStr.lastIndexOf('/'));
            } else {
                assert mainUrlBase != null;
                if (!mainUrlBase.equals(urlStr.substring(0, urlStr.lastIndexOf('/')))) {
                    logger.log(TreeLogger.DEBUG, "Found " + slashedName + " at " + urlStr + " The base location is different from  that of " + mainUrlBase + " Not loading");
                    return null;
                }
            }
            try {
                URLConnection conn = url.openConnection();
                return Util.readURLConnectionAsBytes(conn);
            } catch (IOException ignored) {
                logger.log(TreeLogger.DEBUG, "Unable to load " + urlStr + ", in trying to load " + slashedName);
            }
            return null;
        }
