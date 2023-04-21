    StringBuffer downloadUrl(URL url, String filename) throws IOException {
        StringBuffer buffer = new StringBuffer();
        long start = System.currentTimeMillis();
        System.out.print("Getting " + url.toString() + " => " + filename + "...");
        buffer.append("Getting " + url.toString() + " => " + filename + "...");
        URLConnection connection = url.openConnection();
        connection.connect();
        BufferedInputStream inStream = null;
        FileOutputStream fos = null;
        try {
            inStream = new BufferedInputStream(connection.getInputStream());
            File newFile = new File(filename);
            fos = new FileOutputStream(newFile);
            int read;
            while ((read = inStream.read()) != -1) {
                fos.write(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
        String elapsedTime = new ElapsedTimeCalculator().calculateElapsedTime(start, System.currentTimeMillis());
        System.out.println(elapsedTime);
        buffer.append(elapsedTime + "\n");
        return buffer;
    }
