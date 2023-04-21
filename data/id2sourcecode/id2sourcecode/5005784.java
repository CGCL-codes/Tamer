    public static int openFile(String uri, String userno, String filename, InputStream iis) throws IOException {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DataOutputStream dos = null;
        String query;
        uri += "SukuServlet";
        query = "cmd=file";
        byte[] bytes = query.getBytes();
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        con.setRequestProperty("Referer", "/SSS/" + userno + "/" + filename + "/");
        con.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        con.setRequestMethod("POST");
        dos = new DataOutputStream(con.getOutputStream());
        dos.write(bytes);
        dos.writeBytes(lineEnd);
        dos.writeBytes(lineEnd);
        int nextByte;
        StringBuilder rivi = new StringBuilder();
        while ((nextByte = iis.read()) >= 0) {
            if (rivi.length() > 64) {
                dos.writeBytes(rivi.toString() + lineEnd);
                rivi = new StringBuilder();
            }
            rivi.append(hexi.charAt((nextByte >> 4) & 0xf));
            rivi.append(hexi.charAt((nextByte) & 0xf));
        }
        if (rivi.length() > 0) {
            dos.writeBytes(rivi.toString() + lineEnd);
        }
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        dos.flush();
        dos.close();
        InputStream in = con.getInputStream();
        int inle = 0;
        while (true) {
            int idata = in.read();
            if (idata == -1) break;
            inle++;
        }
        int resu = con.getResponseCode();
        in.close();
        dos.close();
        con.disconnect();
        return resu;
    }
