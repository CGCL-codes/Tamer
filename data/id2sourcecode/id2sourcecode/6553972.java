    private boolean processFile(File f) {
        synchronized (PROCESS_MUTEX) {
            if (bufferFile != null) {
                try {
                    writeToBuffer(f);
                } catch (Exception exc) {
                    bufferOS = null;
                    System.out.println("error:" + exc);
                    addFile(f);
                    return false;
                }
            }
            String urlString = fileUrlTemplate.replace("${file}", f.toString());
            urlString = urlString.replace("${type}", type);
            try {
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                InputStream s = connection.getInputStream();
                String results = IOUtil.readInputStream(s);
                if (!results.equals("OK")) {
                    addFile(f);
                    if (debug) {
                        System.out.println("connection not successful:" + results);
                    }
                    return false;
                }
            } catch (Exception exc) {
                System.out.println("error:" + exc);
                addFile(f);
                return false;
            }
            return true;
        }
    }
