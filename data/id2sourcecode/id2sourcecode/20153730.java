    private Boolean push_file(String tivoName, String share, String path, String push_file) {
        if (file.isFile(push_file)) {
            String header = "http://" + host + ":" + port + "/TiVoConnect?Command=Push&Container=";
            String path_entry;
            if (path.length() == 0) {
                path_entry = "&File=/";
            } else {
                path_entry = "&File=/" + urlEncode(path) + "/";
            }
            String urlString = header + urlEncode(share) + path_entry + urlEncode(string.basename(push_file)) + "&tsn=" + urlEncode(tivoName);
            try {
                URL url = new URL(urlString);
                log.warn(">> Pushing " + push_file + " to " + tivoName);
                log.print(url.toString());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.addRequestProperty("REFERER", "/");
                c.setRequestMethod("GET");
                c.setReadTimeout(timeout_http * 1000);
                c.connect();
                String response = c.getResponseMessage();
                if (response.equals("OK")) {
                    thread_running = false;
                    return true;
                } else {
                    log.error("Received unexpected response for: " + urlString);
                    log.error(response);
                    thread_running = false;
                    return false;
                }
            } catch (Exception e) {
                log.error("Connection failed: " + urlString);
                log.error(e.toString());
            }
        } else {
            log.error("File does not exist - " + push_file);
        }
        thread_running = false;
        return false;
    }
