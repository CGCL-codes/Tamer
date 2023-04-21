    private void loadTocs(String urlName) {
        InputStream is = null;
        if (urlName == null || urlName.length() == 0) {
            resetRemoteTocs();
            return;
        }
        try {
            URL url = new URL(urlName);
            url = new URL(url, "toc/");
            URLConnection connection = url.openConnection();
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            load(reader);
            reader.close();
        } catch (MalformedURLException e) {
            HelpUIPlugin.logError(Messages.InfoCenterPage_invalidURL, e, false, true);
            resetRemoteTocs();
        } catch (IOException e) {
            HelpUIPlugin.logError(Messages.InfoCenterPage_tocError, e, false, true);
            resetRemoteTocs();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
