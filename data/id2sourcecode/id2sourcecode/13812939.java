    public void loadPage() {
        try {
            URL url = new URL(makeURL());
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            htmlSource = "";
            String htmlLine;
            while ((htmlLine = in.readLine()) != null) htmlSource += htmlLine;
            in.close();
        } catch (Exception e) {
        }
    }
