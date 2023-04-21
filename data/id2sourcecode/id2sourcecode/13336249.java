    protected void readROM(String resource, int startMem, int len) {
        try {
            URL url = getClass().getResource(resource);
            monitor.info("URL: " + url);
            monitor.info("Read ROM " + resource);
            if (url == null) url = new URL(codebase + resource);
            loadROM(new DataInputStream(url.openConnection().getInputStream()), startMem, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
