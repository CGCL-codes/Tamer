    static void download(String from, File to) {
        try {
            URL url = new URL(from);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(to);
            int i;
            System.out.println("[NoSpam] Downloading file from " + from + " to " + to);
            while ((i = is.read()) != -1) {
                os.write(i);
            }
            os.flush();
            is.close();
            os.close();
            System.out.println("[NoSpam] Downloading finished!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
