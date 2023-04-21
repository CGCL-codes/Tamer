    public String soapToDigiD(String soapMessage) throws IOException {
        String input = null;
        int c;
        BufferedReader in = null;
        PrintWriter pw = null;
        try {
            if (digidURL.equals("")) return null;
            URL url = new URL(digidURL);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Close");
            connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            pw = new PrintWriter(os);
            pw.println(soapMessage);
            pw.flush();
            displayConnectionHeaders(connection);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (input = ""; ; ) {
                c = in.read();
                if (c < 0) break;
                input = input + (char) c;
            }
        } catch (IOException e) {
            logger.error("I/O Exception: " + e.getMessage(), e);
            throw e;
        } finally {
            if (pw != null) pw.close();
            if (in != null) in.close();
        }
        return input;
    }
