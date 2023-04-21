    public ITocContribution[] getTocContributions(String locale) {
        if (RemoteHelp.isEnabled()) {
            InputStream in = null;
            PreferenceFileHandler prefHandler = new PreferenceFileHandler();
            RemoteTocParser parser = new RemoteTocParser();
            String host[] = prefHandler.getHostEntries();
            String port[] = prefHandler.getPortEntries();
            String path[] = prefHandler.getPathEntries();
            String isEnabled[] = prefHandler.isEnabled();
            ITocContribution[] currentContributions = new ITocContribution[0];
            ITocContribution[] temp = new ITocContribution[0];
            ITocContribution[] totalContributions = new ITocContribution[0];
            int numICs = host.length;
            if (numICs == 0) return new ITocContribution[0];
            URL url = null;
            String urlStr = "";
            for (int i = 0; i < numICs; i++) {
                if (isEnabled[i].equalsIgnoreCase("true")) {
                    try {
                        url = new URL("http", host[i], new Integer(port[i]).intValue(), path[i] + PATH_TOC + '?' + PARAM_LANG + '=' + locale);
                        in = url.openStream();
                        if (in != null) {
                            urlStr = PROTOCOL + host[i] + ":" + port[i] + path[i];
                            currentContributions = parser.parse(in, urlStr);
                            temp = new ITocContribution[totalContributions.length];
                            System.arraycopy(totalContributions, 0, temp, 0, totalContributions.length);
                            totalContributions = new ITocContribution[temp.length + currentContributions.length];
                            System.arraycopy(temp, 0, totalContributions, 0, temp.length);
                            System.arraycopy(currentContributions, 0, totalContributions, temp.length, currentContributions.length);
                        }
                    } catch (Throwable t) {
                        String msg = "Internal error while reading TOC contents from remote server";
                        HelpBasePlugin.logError(msg, t);
                        RemoteHelp.setError(t);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                                in = null;
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
            return totalContributions;
        }
        return new ITocContribution[0];
    }
