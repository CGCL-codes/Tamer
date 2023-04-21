    public void execute(String command, InputStream istream, String contentType, int contentLength) throws TaskExecutionException {
        URLConnection conn;
        InputStreamReader reader = null;
        try {
            logger.info("Task execution using URL = " + url + command);
            conn = (new URL(url + command)).openConnection();
            HttpURLConnection hconn = (HttpURLConnection) conn;
            hconn.setAllowUserInteraction(false);
            hconn.setDoInput(true);
            hconn.setUseCaches(false);
            if (istream != null) {
                hconn.setDoOutput(true);
                hconn.setRequestMethod("PUT");
                if (contentType != null) {
                    hconn.setRequestProperty("Content-Type", contentType);
                }
                if (contentLength >= 0) {
                    hconn.setRequestProperty("Content-Length", "" + contentLength);
                }
            } else {
                hconn.setDoOutput(false);
                hconn.setRequestMethod("GET");
            }
            hconn.setRequestProperty("User-Agent", "Project-Assimilator-Reload-Task/1.0");
            String input = username + ":" + password;
            String output = Base64.encodeBytes(input.getBytes());
            hconn.setRequestProperty("Authorization", "Basic " + output);
            hconn.connect();
            if (istream != null) {
                BufferedOutputStream ostream = new BufferedOutputStream(hconn.getOutputStream(), 1024);
                byte buffer[] = new byte[1024];
                while (true) {
                    int n = istream.read(buffer);
                    if (n < 0) {
                        break;
                    }
                    ostream.write(buffer, 0, n);
                }
                ostream.flush();
                ostream.close();
                istream.close();
            }
            reader = new InputStreamReader(hconn.getInputStream());
            StringBuffer buff = new StringBuffer();
            String error = null;
            boolean first = true;
            while (true) {
                int ch = reader.read();
                if (ch < 0) {
                    break;
                } else if ((ch == '\r') || (ch == '\n')) {
                    String line = buff.toString();
                    buff.setLength(0);
                    logger.fine("Return msg = " + line);
                    if (first) {
                        if (!line.startsWith("OK -")) {
                            error = line;
                        }
                        first = false;
                    }
                } else {
                    buff.append((char) ch);
                }
            }
            if (buff.length() > 0) {
                logger.fine(buff.toString());
            }
            if (error != null) {
                throw new TaskExecutionException(error);
            }
        } catch (Throwable t) {
            throw new TaskExecutionException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            if (istream != null) {
                try {
                    istream.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
