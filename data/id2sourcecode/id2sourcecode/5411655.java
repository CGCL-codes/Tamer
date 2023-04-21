    public TraceFileReader(URL url) {
        try {
            readFile(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            System.out.println("got a readLine Error");
            e.printStackTrace();
            this.status = false;
        }
    }
