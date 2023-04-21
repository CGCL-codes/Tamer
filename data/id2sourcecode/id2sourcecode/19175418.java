    public boolean execute() {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data, boundary=AaB03x");
            DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
            for (String key : postParameters.keySet()) {
                dataOut.writeBytes("--AaB03x\r\n");
                String string = postParameters.get(key);
                dataOut.writeBytes("Content-Length: " + string.length() + "\r\n");
                dataOut.writeBytes("content-disposition: form-data; name=" + key + "\r\n");
                dataOut.writeBytes("\r\n");
                dataOut.writeBytes(string + "\r\n");
            }
            for (String key : postFiles.keySet()) {
                byte[] data = postFiles.get(key);
                dataOut.writeBytes("--AaB03x\r\n");
                dataOut.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"file\"\r\n");
                dataOut.writeBytes("Content-Type: image/jpeg\r\n");
                dataOut.writeBytes("Content-Transfer-Encoding: binary");
                dataOut.writeBytes("Content-Length: " + data.length + "\r\n");
                dataOut.writeBytes("\r\n");
                dataOut.write(data);
                dataOut.writeBytes("\r\n");
            }
            dataOut.writeBytes("--AaB03x--\r\n");
            dataOut.flush();
            dataOut.close();
            statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
