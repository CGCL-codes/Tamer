    public static void UploadToUrl(String url, String path) {
        try {
            String xmldata = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            String line;
            String hostname = url.replace("http://www.", "");
            int port = 80;
            InetAddress addr = InetAddress.getByName(hostname);
            Socket sock = new Socket(addr, port);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            wr.write("POST " + path + " HTTP/1.0\r\n");
            wr.write("Host: " + url + "\r\n");
            wr.write("Content-Length: " + xmldata.length() + "\r\n");
            wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
            wr.write("\r\n");
            BufferedReader rd = new BufferedReader(new FileReader("output.xml"));
            while ((line = rd.readLine()) != null) wr.write(line);
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            while ((line = rd.readLine()) != null) System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
