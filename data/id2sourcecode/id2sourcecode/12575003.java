    private synchronized boolean readURLUpload(final String action, String mediafile, String thumbnail) {
        udebug.dprintln("url action=" + action);
        if (udebug.getVerbose()) {
            udebug.vprintln("Who is making us slow down?");
            (new Exception()).printStackTrace();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        if ((sessionid == null) && (triedConnection)) return false;
        Vector<String> lines = new Vector<String>();
        boolean gotSuccess = false;
        badStatus = false;
        try {
            URL url = new URL(baseURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            String boundary = "--------------" + getRandomString();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary.substring(2, boundary.length()));
            conn.connect();
            OutputStream out = conn.getOutputStream();
            String content = "";
            content = content + boundary + "\r\n";
            content = content + "Content-Disposition: form-data; name=\"action\"\r\n\r\n";
            content = content + action + "\r\n";
            if (sessionid != null) {
                content = content + boundary + "\r\n";
                content = content + "Content-Disposition: form-data; name=\"" + sessionName + "\"\r\n\r\n";
                content = content + sessionid + "\r\n";
            }
            out.write(content.getBytes());
            content = "";
            if (mediafile != null) {
                File testfile = new File(mediafile);
                content = content + boundary + "\r\n";
                content = content + "Content-Disposition: form-data; name=\"mediafile\"; filename=\"" + testfile.getName() + "\"\r\n";
                content = content + "Content-Type: image/jpeg\r\n\r\n";
                out.write(content.getBytes());
                FileInputStream is = new FileInputStream(testfile);
                byte[] buf = new byte[512];
                int res = -1;
                while ((res = is.read(buf)) != -1) {
                    out.write(buf);
                }
                is.close();
                out.write("\r\n".getBytes());
                content = "";
            }
            if (thumbnail != null) {
                File testfile = new File(thumbnail);
                content = content + boundary + "\r\n";
                content = content + "Content-Disposition: form-data; name=\"thumbnail\"; filename=\"" + testfile.getName() + "\"\r\n";
                content = content + "Content-Type: image/jpeg\r\n\r\n";
                out.write(content.getBytes());
                FileInputStream is = new FileInputStream(thumbnail);
                byte[] buf = new byte[512];
                int res = -1;
                while ((res = is.read(buf)) != -1) {
                    out.write(buf);
                }
                is.close();
                out.write("\r\n".getBytes());
            }
            content = boundary + "--\r\n";
            out.write(content.getBytes());
            out.close();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(), encoding);
            BufferedReader data = new BufferedReader(in);
            statusText = data.readLine();
            udebug.dprintln("status = " + statusText);
            if (statusText == null) statusText = STR_ERROR + ": missing CGI output";
            gotSuccess = STR_SUCCESS.equals(statusText);
            String line;
            while ((line = data.readLine()) != null) {
                udebug.vprintln("line = " + line);
                lines.add(line);
            }
            in.close();
            udebug.dprintln("\n");
            if (gotSuccess && (lines.size() > 0)) {
                String first = lines.get(0);
                if (first.indexOf(STR_ERROR) >= 0) gotSuccess = false;
            }
            if (!gotSuccess) {
                errorMessage(action, statusText, "");
                if (statusText.indexOf(STR_ERROR) < 0) {
                    System.err.println("Bad error message: " + statusText);
                    badStatus = true;
                }
                return false;
            }
        } catch (IOException e) {
            statusText = "IO Error: " + e.getMessage();
            System.err.println(statusText);
            badStatus = true;
            return false;
        }
        return true;
    }
