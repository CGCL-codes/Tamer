    void update(String whichText) {
        System.out.println("Making a Twitter update");
        if (System.currentTimeMillis() < twitterTimer + TWITTER_INTERVAL) {
            System.out.println("Too Quick! Post Slower!");
            return;
        }
        String updateText = "status=" + whichText;
        String twitterOutput = "";
        try {
            URL url = new URL("http://twitter.com/statuses/update.xml");
            URLConnection myConnection = url.openConnection();
            myConnection.setRequestProperty("Authorization", "Basic " + encoding);
            myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            myConnection.setRequestProperty("Content-Length", "" + Integer.toString(updateText.getBytes().length));
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            DataOutputStream dos;
            dos = new DataOutputStream(myConnection.getOutputStream());
            dos.writeBytes(updateText);
            dos.flush();
            dos.close();
            DataInputStream dis;
            String inputLine;
            dis = new DataInputStream(myConnection.getInputStream());
            while ((inputLine = dis.readLine()) != null) {
                System.out.println(inputLine);
                twitterOutput += inputLine;
            }
            dis.close();
        } catch (MalformedURLException me) {
            System.out.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
    }
