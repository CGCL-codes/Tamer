    public IContext getContext(String id, String locale) {
        PreferenceFileHandler prefHandler = new PreferenceFileHandler();
        String host[] = prefHandler.getHostEntries();
        String port[] = prefHandler.getPortEntries();
        String path[] = prefHandler.getPathEntries();
        String isEnabled[] = prefHandler.isEnabled();
        if (RemoteHelp.isEnabled()) {
            int numICs = host.length;
            for (int i = 0; i < numICs; i++) {
                if (isEnabled[i].equals("true")) {
                    InputStream in = null;
                    try {
                        URL url = new URL(PROTOCOL, host[i], new Integer(port[i]).intValue(), path[i] + PATH_CONTEXT + '?' + PARAM_ID + '=' + id + '&' + PARAM_LANG + '=' + locale);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        if (connection.getResponseCode() == 200) {
                            in = connection.getInputStream();
                            if (reader == null) {
                                reader = new DocumentReader();
                            }
                            return (Context) reader.read(in);
                        }
                    } catch (IOException e) {
                        String msg = "I/O error while trying to contact the remote help server";
                        HelpBasePlugin.logError(msg, e);
                    } catch (Throwable t) {
                        String msg = "Internal error while reading search results from remote server";
                        HelpBasePlugin.logError(msg, t);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
