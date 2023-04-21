    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: bin/run.sh org.exist.examples.http.PutExample <fileid>");
            System.exit(0);
        }
        String fileName = args[0];
        File file = new File(fileName);
        if (!file.canRead()) {
            System.err.println("Cannot read file " + file);
            return;
        }
        String docName = file.getName();
        try {
            URL url = new URL("http://localhost:8080/exist/rest" + DBBroker.ROOT_COLLECTION + "/test/" + docName);
            System.out.println("PUT file to " + url.toString());
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("PUT");
            connect.setDoOutput(true);
            connect.setRequestProperty("ContentType", "application/xml");
            OutputStream os = connect.getOutputStream();
            InputStream is = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int c;
            while ((c = is.read(buf)) > -1) {
                os.write(buf, 0, c);
            }
            os.flush();
            os.close();
            System.out.println("Statuscode " + connect.getResponseCode() + " (" + connect.getResponseMessage() + ")");
            System.out.println("GET file from " + url.toString());
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            System.out.println("Result:");
            BufferedReader bis = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;
            while ((line = bis.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
