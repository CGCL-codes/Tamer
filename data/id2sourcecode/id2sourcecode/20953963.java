    public void setPage(String URI) {
        try {
            URL url = new URL(URI);
            URLConnection site = url.openConnection();
            BufferedReader bin = new BufferedReader(new InputStreamReader(site.getInputStream()));
            StringBuffer filecontents = new StringBuffer();
            String boxfill;
            while ((boxfill = bin.readLine()) != null) {
                filecontents.append(boxfill + "\n");
            }
            createNewDocument(guessFileType(URI), filecontents.toString());
            setCaretPosition(0);
        } catch (Exception e) {
            System.err.println("Could not load file because: " + e);
            tellMessage(e.toString());
        }
    }
