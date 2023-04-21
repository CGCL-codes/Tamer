    public String doHttpReq(String urlStr) {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            is = url.openStream();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            throw new MySpaceException(sw.toString(), MySpaceException.CONNECT_FAILED);
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
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
        String response = sb.toString();
        return response;
    }
