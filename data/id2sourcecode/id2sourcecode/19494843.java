    private void postFile() {
        try {
            URL url = new URL(baseUrl + servicePath + attributes);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            FileInputStream in = null;
            DataInputStream din = null;
            OutputStream out = null;
            int fileLength = (int) uploadFile.length();
            data = new byte[fileLength];
            in = new FileInputStream(uploadFile);
            try {
                din = new DataInputStream(in);
                din.readFully(data);
            } catch (EOFException e) {
                answer = "EOFException: ";
                return;
            }
            out = connection.getOutputStream();
            out.write(data);
            out.flush();
            in.close();
            din.close();
            out.close();
            BufferedReader bufr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = null;
            StringBuffer buffer = new StringBuffer();
            while ((response = bufr.readLine()) != null) {
                buffer.append(response);
            }
            answer = buffer.toString();
        } catch (MalformedURLException e) {
            answer = "Malformed Url: " + e.getMessage();
            return;
        } catch (IOException e) {
            answer = "post I/O exception: " + e.getMessage();
            return;
        }
    }
