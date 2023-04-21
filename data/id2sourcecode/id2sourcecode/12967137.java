    public void setUpload(String filename) {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        InputStream is = null;
        OutputStream os = null;
        boolean ret = false;
        String StrMessage = "";
        String exsistingFileName = filename;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerUploadFile";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName));
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"upload\";" + " filename=\"" + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            System.out.println("From ServletCom CLIENT REQUEST:" + ex);
        } catch (IOException ioe) {
            System.out.println("From ServletCom CLIENT REQUEST:" + ioe);
        }
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;
            while ((str = inStream.readLine()) != null) {
            }
            inStream.close();
        } catch (IOException ioex) {
            System.out.println("From (ServerResponse): " + ioex);
        }
    }
