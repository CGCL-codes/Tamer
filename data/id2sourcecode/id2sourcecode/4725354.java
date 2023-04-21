    public String completeLocation(String location) {
        int ic = location.indexOf(":");
        if (ic < 2 || ic > location.indexOf("/")) {
            String[] paths = bundleDirs;
            for (int i = 0; i < paths.length; i++) {
                try {
                    URL url = new URL(new URL(paths[i]), location);
                    if ("file".equals(url.getProtocol())) {
                        File f = new File(url.getFile());
                        if (!f.exists() || !f.canRead()) {
                            continue;
                        }
                    } else if ("http".equals(url.getProtocol())) {
                        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                        uc.connect();
                        int rc = uc.getResponseCode();
                        uc.disconnect();
                        if (rc != HttpURLConnection.HTTP_OK) {
                            continue;
                        }
                    } else {
                        InputStream is = null;
                        try {
                            is = url.openStream();
                        } finally {
                            if (is != null) is.close();
                        }
                    }
                    location = url.toString();
                    break;
                } catch (Exception _e) {
                }
            }
        }
        return location;
    }
