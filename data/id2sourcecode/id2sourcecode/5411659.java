    public void initReader(URL url) {
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            System.out.println("got a readLine Error");
            e.printStackTrace();
            this.status = false;
        }
    }
