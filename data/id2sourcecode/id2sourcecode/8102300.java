    private InputStream getInputStream(DataInputType type, String input) throws ApolloAdapterException {
        InputStream stream = null;
        if (input == null) {
            String message = "The input was null. Neither filename nor URL given";
            logger.warn(message);
            throw new ApolloAdapterException(message);
        }
        if (type == DataInputType.FILE) {
            try {
                String path = apollo.util.IOUtil.findFile(input, false);
                logger.info("Trying to open file " + input);
                stream = new FileInputStream(path);
            } catch (Exception ex) {
                throw new ApolloAdapterException("Error: could not open " + input + " for reading.");
            }
        } else if (type == DataInputType.URL) {
            String query_str = null;
            if (!input.startsWith("http://")) {
                Hashtable pubDbToURL = Config.getPublicDbList();
                String url_str = (String) pubDbToURL.get(getDatabase());
                logger.debug("database " + database + " URL is " + url_str);
                if (url_str != null) {
                    if (!url_str.startsWith("http://")) url_str = "http://" + url_str;
                    int index = url_str.indexOf('*');
                    if (index > 0) {
                        query_str = url_str.substring(0, index) + input;
                        index++;
                        if (index < url_str.length()) query_str = query_str + url_str.substring(index);
                    } else {
                        query_str = url_str + input;
                    }
                }
            } else {
                query_str = input;
            }
            try {
                logger.info("Trying to open URL " + query_str);
                URL url = new URL(query_str);
                stream = url.openStream();
            } catch (Exception ex1) {
                stream = null;
                throw new ApolloAdapterException("Error: could not open " + input + " for reading using \"" + query_str + "\"");
            }
        }
        return stream;
    }
