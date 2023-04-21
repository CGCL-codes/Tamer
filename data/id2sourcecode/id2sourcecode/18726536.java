        public void setURL(String path) {
            this.path = path;
            Resource res = ResourceLoader.getResource(path);
            if (res != null && res.getURL() != null) {
                url = res.getURL();
                try {
                    InputStream in = url.openStream();
                    in.close();
                    OSPLog.finer(LaunchRes.getString("Log.Message.URL") + " " + url);
                } catch (Exception ex) {
                    url = null;
                }
            } else url = null;
        }
