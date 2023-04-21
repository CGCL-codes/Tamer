        long getLastModified(String stylesheetId) {
            long lastModified = Long.MIN_VALUE;
            URL url = null;
            try {
                url = new URL(stylesheetId);
                if (url.getProtocol().equals("file")) {
                    File file = new File(url.getFile());
                    lastModified = file.lastModified();
                } else {
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    lastModified = conn.getLastModified();
                }
            } catch (MalformedURLException e) {
                System.err.println("Invalid URL " + url + ": " + e.toString());
            } catch (IOException e) {
                System.err.println("Cannot access " + url + ": " + e.toString());
            }
            return lastModified;
        }
