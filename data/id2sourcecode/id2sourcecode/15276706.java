    @Override
    public void run() {
        System.out.println("Validating url: " + url);
        try {
            urls = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            code = conn.getResponseCode();
            if (code == 200) {
                System.out.println(urls + " is valid");
            } else {
                synchronized (mutex) {
                    bw1 = new BufferedWriter(new FileWriter("./invalid_urls.txt", true));
                    bw1.write(filepath + ";" + linenum + ";" + url + ";HttpCode:" + code + LINE_SEP);
                    bw1.close();
                }
                System.err.println(url + " is invalid, code is " + code);
            }
        } catch (Exception ex) {
            synchronized (mutex) {
                try {
                    bw1 = new BufferedWriter(new FileWriter("./invalid_urls.txt", true));
                    bw1.write(filepath + ";" + linenum + ";" + url + ";Exception: " + ex.getClass().getName() + LINE_SEP);
                    bw1.close();
                } catch (IOException ex1) {
                }
            }
            System.err.println(url + " is invalid, with Exception: " + ex.getMessage() + "," + ex.getCause());
        }
    }
