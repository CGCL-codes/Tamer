    private void load() {
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(fileName);
            URLConnection urlConnection = url.openConnection();
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
            reader = new BufferedReader(isr);
        } catch (Exception err) {
            isSourceWorking = false;
            status = new StringBuffer();
            status.append("ERROR: Can't connect to URL ");
            status.append(fileName);
            return;
        }
        try {
            parseFile(reader);
            status = new StringBuffer();
            status.append("URL ");
            status.append(url.toString());
            status.append(" is accessible and ");
            status.append(" the page contents can be ");
            status.append("interpretted correctly.");
            isSourceWorking = true;
        } catch (Exception err) {
            status = new StringBuffer();
            status.append("URL is valid but data could not be interpretted properly");
            isSourceWorking = false;
        }
    }
