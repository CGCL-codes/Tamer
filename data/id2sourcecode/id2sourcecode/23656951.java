    @Override
    public void run() {
        OutputStream out;
        try {
            out = new FileOutputStream(destFile);
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            int inputLine;
            while ((inputLine = in.read()) != -1) {
                out.write(inputLine);
            }
            out.close();
            System.out.println("FINISHED: " + destFile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
