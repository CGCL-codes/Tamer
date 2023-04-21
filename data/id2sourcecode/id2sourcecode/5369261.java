    public StringBuilder getPage(String url) {
        BufferedReader reader = null;
        try {
            pageBuffer.delete(0, pageBuffer.length());
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            prepareConnection(httpConn);
            httpConn.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                pageBuffer.append(line);
            }
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
