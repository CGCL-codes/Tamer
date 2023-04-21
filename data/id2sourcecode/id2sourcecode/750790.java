    public void run() {
        try {
            NumberFormat nf = NumberFormat.getNumberInstance();
            lastRead = new Date();
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                String verString = DataStore.getInstance().getVersion();
                con = (HttpURLConnection) webPage.openConnection();
                con.setRequestProperty("User-Agent", "TVSchedulerPro(" + verString + ")");
                is = con.getInputStream();
            } catch (Exception e) {
                System.out.println("ERROR: Url Exception (" + e.toString() + ")");
                finished = true;
                return;
            }
            returnCode = con.getResponseCode();
            int colCount = 0;
            lastRead = new Date();
            byte[] buff = new byte[128];
            int read = is.read(buff);
            while (read > -1) {
                if (colCount == 80) {
                    System.out.println("Downloaded: " + nf.format(pageBytes.size()));
                    colCount = 0;
                }
                colCount++;
                lastRead = new Date();
                pageBytes.write(buff, 0, read);
                read = is.read(buff);
            }
            System.out.println("Downloaded: " + nf.format(pageBytes.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finished = true;
    }
