    public static boolean deploy(JSONObject con, File deployFile) {
        boolean success = false;
        String urlstring = con.optString("url");
        if (con.optString("vendor").equals("Activiti")) {
            urlstring += "/deployment";
        }
        URL url;
        try {
            BASE64Encoder enc = new BASE64Encoder();
            String userpassword = con.optString("username") + ":" + con.optString("password");
            String encodedAuthorization = enc.encode(userpassword.getBytes());
            String boundary = "**#Yaoqiang$**";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            FileInputStream fileInputStream = new FileInputStream(deployFile);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("--" + boundary + "\r\nContent-Disposition: form-data; name=\"success\"\r\n\r\n");
            dos.writeBytes("success");
            dos.writeBytes("\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=\"failure\"\r\n\r\n");
            dos.writeBytes("failure");
            String filename = BPMNEditor.getCurrentFile().getName();
            if (filename.endsWith(".bpmn20.xml")) {
                filename = filename.substring(0, filename.lastIndexOf(".bpmn20.xml")) + ".bar";
            } else {
                filename = filename.substring(0, filename.lastIndexOf(".")) + ".bar";
            }
            dos.writeBytes("\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=\"deployment\"; filename=\"" + filename + "\"\r\n\r\n");
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes("\r\n--" + boundary + "--\r\n");
            fileInputStream.close();
            dos.flush();
            dos.close();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String tempLine = rd.readLine();
                StringBuffer temp = new StringBuffer();
                while (tempLine != null) {
                    temp.append(tempLine);
                    tempLine = rd.readLine();
                }
                rd.close();
                is.close();
                success = temp.indexOf("success()") > 0;
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
