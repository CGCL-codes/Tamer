    @Override
    public void run() {
        super.run();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Internet Explorer");
            String sProperty = "bytes=" + startPosition + "-";
            con.setRequestProperty("Range", sProperty);
            con.setReadTimeout(6000);
            InputStream inputStream = con.getInputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            int readFileSize = 0;
            System.out.println(this.getName() + ":" + this.block);
            while (readFileSize <= block && (len = inputStream.read(buffer)) != -1) {
                threadFile.write(buffer, 0, len);
                readFileSize += len;
            }
            threadFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
