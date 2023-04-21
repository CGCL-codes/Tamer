    public void displayHtmlSource(String urlName) {
        String s;
        StringBuffer buf = new StringBuffer();
        try {
            URL url = new URL(urlName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((s = in.readLine()) != null) buf.append(s + "\n");
            tA.setText(buf.toString());
        } catch (MalformedURLException e) {
            System.err.println("Error 1 :)");
        } catch (FileNotFoundException e) {
            System.err.println("Error 2 :)");
        } catch (IOException e) {
            System.err.println("Error 3 :)");
        }
    }
