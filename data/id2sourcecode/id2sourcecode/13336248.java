    public void loadDebug(String resource) {
        try {
            URL url = getClass().getResource(resource);
            monitor.info("Loading debug from URL: " + url);
            if (url == null) url = new URL(codebase + resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                int adr = -1;
                try {
                    adr = Integer.parseInt(parts[0].trim(), 16);
                } catch (Exception e) {
                }
                if (adr != -1) {
                    setDebug(adr, parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load debug text: " + resource);
        }
    }
