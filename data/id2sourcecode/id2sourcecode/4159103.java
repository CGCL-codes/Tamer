    public String GetUserPage(String User) {
        int page = 1;
        URL url;
        String line, finalstring;
        StringBuffer buffer = new StringBuffer();
        setStatus("Start moling....");
        startTimer();
        try {
            url = new URL(HTMLuserpage + User + "?setcount=100&page=" + page);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.addRequestProperty("User-Agent", userAgent);
            System.out.println("moling: page of " + User);
            BufferedReader input = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            input.close();
            connect.disconnect();
            stopTimer();
            setStatus("Dauer : " + dauerMs() + " ms");
            finalstring = buffer.toString();
            return finalstring;
        } catch (MalformedURLException e) {
            System.err.println("Bad URL: " + e);
            return null;
        } catch (IOException io) {
            System.err.println("IOException: " + io);
            return null;
        }
    }
