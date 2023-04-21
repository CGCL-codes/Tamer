    public void writeToDeveloper() {
        String developperURL = "http://osm.youseeus.de/osmolt/bug/send.php";
        URL url;
        try {
            url = new URL(developperURL);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            String content = "bugtrace=" + message + "\nComment:\n" + comment.getText();
            gui.printDebugMessage("osmoltGui", "sending form to HTTP server ...");
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            try {
                gui.printDebugMessage("osmoltGui", "reading HTML from HTTP server ...");
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.printDebugMessage("osmoltGui", "done.");
            OsmoltGui.osmoltGui.printMessage("Error sent successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
