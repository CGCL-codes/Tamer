    protected String[] getFrameworkBundleLocations() {
        Reader reader = null;
        List<String> locationList = new ArrayList<String>();
        String config = null;
        try {
            reader = new FileReader(platformConfigFileLocation);
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
            config = writer.toString();
        } catch (IOException err) {
            throw new RuntimeException(err);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Throwable err) {
            }
        }
        int start = config.indexOf("<extraLocations>");
        if (start < 0) return null;
        int stop = start;
        String startTag = "<location path=\"";
        String stopTag = "\"/>";
        while ((start = config.indexOf(startTag, start)) > 0) {
            stop = config.indexOf(stopTag, start);
            if (stop <= 0) break;
            locationList.add(config.substring(start + startTag.length(), stop));
            start = stop + stopTag.length();
        }
        String[] locations = new String[locationList.size()];
        locationList.toArray(locations);
        return locations;
    }
