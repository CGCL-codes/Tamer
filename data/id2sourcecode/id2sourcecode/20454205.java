    public void read(URL url, Thread afterReading) throws Exception {
        read(url.toString(), url.openConnection().getInputStream(), afterReading);
    }
