    public void download() {
        String nextLine;
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        try {
            url = new URL("http://86.87.28.143/GIGASET_HTTP/chatlog.txt");
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff = new BufferedReader(inStream);
            while (true) {
                nextLine = buff.readLine();
                if (nextLine != null) {
                    System.out.println(nextLine);
                    Main.chattext = nextLine;
                } else {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("URL lijkt niet te kloppen:" + e.toString());
        } catch (IOException e1) {
            System.out.println("Je internet is kapot!: " + e1.toString());
        }
    }
