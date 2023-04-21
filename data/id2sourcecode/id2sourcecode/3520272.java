    private void getRemoteFileList() {
        String urlstr = updateURL + updateFile;
        URL url = null;
        try {
            url = new URL(urlstr);
            if (url != null) {
                URLConnection con;
                try {
                    Logger.msg(threadName + "getting remote file list");
                    con = url.openConnection();
                    con.setConnectTimeout(5000);
                    BufferedReader d = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = "";
                    while (null != (line = d.readLine())) {
                        String[] split = line.split(";");
                        if (split.length < 3) {
                            Logger.msg(threadName + "split-length < 3");
                        }
                        UpdateFile remoteFile = new UpdateFile(split[0], split[1], Integer.parseInt(split[2]));
                        remoteFilesList.add(remoteFile);
                    }
                    d.close();
                } catch (IOException e1) {
                    Logger.msg(threadName + "Error while retrieving " + urlstr + " (possibly no connection to the internet)");
                }
            }
        } catch (MalformedURLException e) {
            Logger.err(threadName + "URL invalid: " + urlstr);
        }
    }
