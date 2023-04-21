    boolean checkAlive() {
        System.out.println("Checking your Authentication");
        String twitterOutput = "";
        try {
            URL url = new URL("http://www.twitter.com/account/verify_credentials");
            URLConnection myConnection = url.openConnection();
            myConnection.setRequestProperty("Authorization", "Basic " + encoding);
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
            String inputLine;
            System.out.println("connection: " + myConnection.toString());
            while (true) {
                System.out.println("reading line..");
                inputLine = in.readLine();
                if (inputLine == null) {
                    System.out.println("nothing there anymore");
                    break;
                }
                System.out.println("hello" + inputLine);
                twitterOutput += inputLine;
            }
            in.close();
        } catch (MalformedURLException me) {
            System.out.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
        if (twitterOutput.equals("Authorized")) {
            System.out.println("Twitter Authutication had been Verifyed");
            return true;
        } else {
            System.out.println("Access to Twitter API has been Denied");
            return false;
        }
    }
