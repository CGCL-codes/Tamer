    public void writeURL(String fileName, int which) {
        if (rePrintAnswer) printAnswer();
        try {
            URL url = new URL(getCodeBase().toString() + "fdWrite.cgi");
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            String content = "name=" + URLEncoder.encode(fileName) + "&text=" + URLEncoder.encode(which == 1 ? textArea1.getText() : textArea2.getText());
            out.writeBytes(content);
            out.flush();
            out.close();
            DataInputStream in = new DataInputStream(con.getInputStream());
            String s;
            while ((s = in.readLine()) != null) {
            }
            in.close();
        } catch (IOException err) {
        }
    }
