    private String sendAndReceive(String send) throws IOException {
        StringBuilder answer = new StringBuilder();
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(send);
        osw.close();
        if (protocolFile != null) Files.appendToFile(protocolFile, "DIG code send to reasoner:\n\n" + send + "\n\n");
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        do {
            line = br.readLine();
            if (line != null) answer.append(line);
        } while (line != null);
        br.close();
        if (protocolFile != null) Files.appendToFile(protocolFile, "DIG code received from reasoner:\n\n" + answer + "\n\n");
        return answer.toString();
    }
