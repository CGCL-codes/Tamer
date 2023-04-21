    public p2p() {
        String newlist;
        String tempip;
        int tempportno;
        boolean connectedtoserver = false;
        CPoller c;
        SPoller as = new SPoller();
        as.addClientListener(this);
        while (true) {
            try {
                ss = new ServerSocket(serverportno);
                ss.close();
                break;
            } catch (Exception e) {
                serverportno = serverportno + 1;
                e.printStackTrace();
            }
        }
        System.out.println("Server bound to :" + serverportno);
        try {
            ld = new Loader("g:/newproj/tp.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Properties :" + ld.getIPeerIp() + " " + ld.getDirectory() + " " + ld.getClassName());
        IClient a = new IClient();
        String listip = a.getParent(ld.getIPeerIp(), 3000, serverportno);
        System.out.println(listip);
        StringTokenizer st = new StringTokenizer(listip);
        String type = st.nextToken();
        newlist = new String();
        if (type.equalsIgnoreCase("setParent")) {
            newlist = listip.substring(9);
            System.out.println(newlist);
        }
        as.listenForConnection(serverportno);
        StringTokenizer ips = new StringTokenizer(newlist);
        c = new CPoller();
        c.addServerListener(this);
        if (ips.countTokens() == 0) {
            String j = c.connect("localhost", serverportno);
            connectedtoserver = true;
            System.out.println("connected to the local host");
        } else {
            while (ips.hasMoreTokens()) {
                try {
                    tempip = ips.nextToken();
                    tempportno = Integer.parseInt((String) ips.nextElement());
                    c.connect(tempip, tempportno);
                    connectedtoserver = true;
                    System.out.println("connected to " + tempportno);
                    break;
                } catch (Exception e) {
                    tempip = ips.nextToken();
                    tempportno = Integer.parseInt((String) ips.nextToken());
                    e.printStackTrace();
                }
            }
        }
        if (connectedtoserver = false) {
            String j = c.connect("localhost", serverportno);
            System.out.println("connected to the local host by default");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String temp = br.readLine();
                System.out.println(temp);
                if (temp.equalsIgnoreCase("execute")) {
                    startJob();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
