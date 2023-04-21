    private void sendFileThroughWebThread() {
        InputStream is;
        OutputStream os;
        HttpConnection sc;
        exceptionText = null;
        String host = "filetransfer.jimm.org";
        String url = "http://" + host + "/__receive_file.php";
        try {
            sc = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
            sc.setRequestMethod(HttpConnection.POST);
            String boundary = "a9f843c9b8a736e53c40f598d434d283e4d9ff72";
            sc.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            os = sc.openOutputStream();
            StringBuffer buffer2 = new StringBuffer();
            buffer2.append("--").append(boundary).append("\r\n");
            buffer2.append("Content-Disposition: form-data; name=\"jimmfile\"; filename=\"").append(shortFileName).append("\"\r\n");
            buffer2.append("Content-Type: application/octet-stream\r\n");
            buffer2.append("Content-Transfer-Encoding: binary\r\n");
            buffer2.append("\r\n");
            os.write(Util.stringToByteArray(buffer2.toString(), true));
            byte[] buffer = new byte[1024];
            int counter = fsize;
            do {
                int read = fis.read(buffer);
                os.write(buffer, 0, read);
                counter -= read;
                if (fsize != 0) {
                    int percent = 100 * (fsize - counter) / fsize;
                    SplashCanvas.setProgress(percent);
                    SplashCanvas.setMessage(ResourceBundle.getString("ft_transfer") + " " + percent + "% / " + fsize / 1024 + "KB");
                }
            } while (counter > 0);
            StringBuffer buffer3 = new StringBuffer();
            buffer3.append("\r\n--").append(boundary).append("--\r\n");
            os.write(Util.stringToByteArray(buffer3.toString(), true));
            os.flush();
            int respCode = sc.getResponseCode();
            if (respCode != HttpConnection.HTTP_OK) throw new Exception("Server error: " + respCode + "\r\n" + sc.getResponseMessage());
            is = sc.openInputStream();
            StringBuffer response = new StringBuffer();
            for (; ; ) {
                int read = is.read();
                if (read == -1) break;
                response.append((char) (read & 0xFF));
            }
            String respString = response.toString();
            int dataPos = respString.indexOf("\r\n\r\n");
            if (dataPos == -1) {
            } else respString = Util.replaceStr(respString.substring(dataPos + 4), "\r\n", "");
            os.close();
            is.close();
            sc.close();
            System.out.println(respString);
            StringBuffer messText = new StringBuffer();
            messText.append("Filename: ").append(shortFileName).append("\n");
            messText.append("Filesize: ").append(fsize / 1024).append("KB\n");
            messText.append("Link: ").append(respString);
            PlainMessage plainMsg = new PlainMessage(Options.getString(Options.OPTION_UIN), cItem, Message.MESSAGE_TYPE_NORM, Util.createCurrentDate(false), messText.toString());
            Icq.requestAction(new SendMessageAction(plainMsg));
        } catch (Exception e) {
            exceptionText = e.toString();
        }
        curMode = MODE_BACK_TO_MENU;
        Jimm.display.callSerially(this);
    }
