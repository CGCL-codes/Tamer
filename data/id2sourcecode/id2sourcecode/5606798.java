    public void run() {
        URL url = null;
        URLConnection urlConn = null;
        DataOutputStream dOut = null;
        BufferedReader bInp = null;
        try {
            url = new URL(uploadURL);
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            String boundary = "-----------------------------" + getRandomString();
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary.substring(2, boundary.length()));
            String CRLF = "\r\n";
            dOut = new DataOutputStream(urlConn.getOutputStream());
            StringBuffer sb;
            File f;
            uploadedLength = 0;
            progress.setMaximum((int) totalFilesLength);
            for (int i = 0; i < aTotalFiles.length && !stop; i++) {
                f = aTotalFiles[i];
                sb = new StringBuffer();
                sb.append(boundary);
                sb.append(CRLF);
                sb.append("Content-Disposition: form-data; name=\"File");
                sb.append(i);
                sb.append("\"; filename=\"");
                sb.append(f.toString());
                sb.append("\"");
                sb.append(CRLF);
                sb.append("Content-Type: application/octet-stream");
                sb.append(CRLF);
                sb.append(CRLF);
                dOut.writeBytes(sb.toString());
                uploadFileStream(f, dOut);
                dOut.writeBytes(CRLF);
            }
            dOut.writeBytes(boundary);
            dOut.writeBytes("--");
            dOut.writeBytes(CRLF);
            dOut.flush();
            if (!stop) progress.setString("File(s) uploaded. Wait for server response!");
            bInp = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String str;
            while (null != ((str = bInp.readLine()))) {
                this.addServerOutPut(str);
            }
        } catch (Exception e) {
            this.e = e;
        } finally {
            try {
                bInp.close();
            } catch (Exception e) {
            }
            bInp = null;
            try {
                dOut.close();
            } catch (Exception e) {
            }
            dOut = null;
            urlConn = null;
            url = null;
        }
    }
