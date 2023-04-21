        public void run() {
            byte[] msg = new byte[2048];
            HerbivoreUnicastDatagramServerSocket serverSocket = myclique.createUnicastDatagramServerSocket(WEBPROXY_REQUEST_PORT);
            while (true) {
                byte[] input = serverSocket.receive();
                System.out.println("[" + (new Date()) + "] (" + input.length + ") <<" + (new String(input)) + ">>");
                String requestedFile = new String(input);
                try {
                    URL url = new URL(requestedFile);
                    System.out.println("Fetching " + requestedFile);
                    InputStream s = url.openStream();
                    byte[] filecontentbytes = new byte[2000];
                    int n = s.read(filecontentbytes);
                    s.close();
                    System.out.println("Fetched " + requestedFile + " " + n + " bytes.");
                    String filecontent = new String(filecontentbytes);
                    HerbivoreBroadcastStreamSocket output = myclique.createBroadcastStreamSocket(WEBPROXY_RESPONSE_PORT);
                    byte[] mymsg = filecontent.getBytes();
                    System.out.println("Sending " + mymsg.length + " bytes");
                    output.write(mymsg);
                    output.flush();
                    output.close();
                } catch (Exception e) {
                    Log.exception(e);
                }
            }
        }
