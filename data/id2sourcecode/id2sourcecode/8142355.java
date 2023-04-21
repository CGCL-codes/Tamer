    @SuppressWarnings("unused")
    private String post(String url, String content) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream outputStream = new DataOutputStream(httpConn.getOutputStream());
            outputStream.writeBytes(content);
            outputStream.close();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            for (String str = inputStream.readLine(); str != null; str = inputStream.readLine()) {
                buffer.append(str);
            }
            inputStream.close();
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
