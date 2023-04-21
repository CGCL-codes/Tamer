    public StringBuilder postPage(String url, String content) {
        BufferedReader reader = null;
        try {
            pageBuffer.delete(0, pageBuffer.length());
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            prepareConnection(httpConn);
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream outputStream = new DataOutputStream(httpConn.getOutputStream());
            outputStream.writeBytes(content);
            outputStream.close();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                pageBuffer.append(line);
            }
            reader.close();
            return pageBuffer;
        } catch (Throwable error) {
            pageBuffer.delete(0, pageBuffer.length());
            System.out.println("URLExtractor.getPage(): " + error);
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
