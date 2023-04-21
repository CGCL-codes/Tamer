    public void post() {
        netError = false;
        try {
            URL url = new URL(server);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (!urlencoded) conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            out = new ByteArrayOutputStream();
            String line;
            while ((line = parseline()) != null) writeLine(line);
            if (!urlencoded) {
                String term = "--" + boundary + "--";
                out.write(term.getBytes());
                conn.setRequestProperty("Content-Length", "" + ((ByteArrayOutputStream) out).size());
            }
            OutputStream hout = conn.getOutputStream();
            ((ByteArrayOutputStream) out).writeTo(hout);
            out.close();
            hout.close();
            InputStream is = conn.getInputStream();
            int nmax = 100000;
            int nread;
            byte b[] = new byte[nmax];
            serverMsg = "";
            while ((nread = is.read(b)) >= 0) serverMsg = serverMsg + new String(b, 0, nread);
            System.out.print(serverMsg);
        } catch (Exception ex) {
            System.err.println(ex);
            netError = true;
        }
    }
