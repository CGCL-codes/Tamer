    private void processURIElement(URIElement element) {
        String uriString = element.getURI();
        FileManager.writeToLog("Prefetcher looking up -> " + element.toString());
        if (!FileManager.isPresent(uriString)) {
            FileManager.writeToLog("... Prefetching");
            try {
                URL url = new URL(uriString);
                URLConnection uc = url.openConnection();
                InputStream is = uc.getInputStream();
                FileManager.add(uriString, element, is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FileManager.writeToLog("... No need to prefetch");
        }
    }
