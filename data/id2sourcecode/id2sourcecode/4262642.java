    private static String sendPostRequest(String path, ArrayList<String> parameterList) {
        String res = "";
        try {
            URL url = URI.create(path).toURL();
            String data = "";
            Iterator<String> i = parameterList.iterator();
            while (i.hasNext()) {
                if (data.length() == 0) {
                    data = i.next();
                } else {
                    data = data + AND_CHAR + i.next();
                }
                log.log(Level.INFO, "sendPostRequest() data: " + data);
            }
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                res = res + line;
                log.log(Level.INFO, "sendPostRequest() line: " + line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            log.log(Level.INFO, "sendPostRequest() error: " + e.getMessage());
        }
        return res;
    }
