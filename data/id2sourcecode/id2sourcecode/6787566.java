    public String doHttpMethodReq(String urlStr, String requestMethod, String paramStr, Map<String, String> header) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (requestMethod != null) conn.setRequestMethod(requestMethod);
            if (header != null) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            OutputStreamWriter wr = null;
            if (requestMethod != null && !requestMethod.equals("GET") && !requestMethod.equals("DELETE")) {
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(paramStr);
                wr.flush();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            do {
                String line = null;
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    throw new MySpaceException(e.getMessage(), MySpaceException.REQUEST_FAILED);
                }
                if (line == null) break;
                sb.append(line).append("\n");
            } while (true);
            if (wr != null) wr.close();
            if (br != null) br.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            throw new MySpaceException(sw.toString(), MySpaceException.REMOTE_ERROR);
        }
        String response = sb.toString();
        return response;
    }
