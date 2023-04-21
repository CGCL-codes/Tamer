    public static boolean getFile(String BASE_URL, String a_sourceFilename, String a_targetDir) throws Exception {
        String filename = FileKit.getFilename(a_sourceFilename);
        String destFilename = a_targetDir + filename;
        long offset;
        File f = new File(destFilename);
        if (f.exists()) {
            offset = f.length();
        } else {
            offset = 0;
        }
        String a_url = BASE_URL + "download=";
        String requestURL = a_url + a_sourceFilename;
        requestURL = GridKit.addURLParameter(requestURL, "offset", offset);
        URL url1 = new URL(requestURL);
        URL url = new URL(url1.toExternalForm());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        boolean append;
        if (offset == 0) {
            append = false;
        } else {
            append = true;
        }
        InputStream in = con.getInputStream();
        ObjectInputStream sis = new ObjectInputStream(new BufferedInputStream(in, 1024 * 5));
        Status s;
        FileOutputStream fos = new FileOutputStream(destFilename, append);
        long currentOffset = 0;
        try {
            int loopIndex = 0;
            do {
                s = (Status) sis.readObject();
                if (s.code == 0) {
                    if (loopIndex == 0) {
                        if (s.buffer == null || s.buffer.length < 1) {
                            System.out.println("File already exists");
                            return true;
                        }
                    }
                } else if (s.code < 0) {
                    throw new IOException(s.description);
                }
                loopIndex++;
                fos.write(s.buffer);
                currentOffset += s.buffer.length;
                if (s.code == 0) {
                    break;
                }
            } while (true);
        } catch (SocketException sex) {
            System.err.println("Connection to server lost" + " - file transfer interrupted (resum possible)");
            sex.printStackTrace();
            fos.close();
            return false;
        }
        fos.close();
        System.out.println("File received: " + destFilename);
        return true;
    }
