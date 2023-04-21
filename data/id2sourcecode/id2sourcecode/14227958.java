    public static String[][] getChannelData(String ipStr) {
        try {
            InetAddress ip = InetAddress.getByName(ipStr);
            Socket socket = new Socket(ip, 724);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("GETSERVERS");
            String line = in.readLine();
            if (line == null) {
                return null;
            } else if (line.equals("0")) {
                return new String[0][8];
            } else if (isInteger(line)) {
                System.out.println(line);
                int numServers = Integer.parseInt(line);
                String[][] servers = new String[numServers][8];
                for (int x = 0; x < numServers; x++) {
                    String chanData = in.readLine();
                    if (chanData != null && isChannelDataValid(chanData)) {
                        System.out.println("Channel data valid.");
                        servers[x][0] = getServerIP(chanData);
                        servers[x][1] = getServerPort(chanData);
                        for (int y = 2; y < 8; y++) {
                            servers[x][y] = readQuotes(chanData, y);
                            System.out.println(servers[x][y]);
                        }
                    } else {
                        System.out.println("Channel data invalid.");
                        System.out.println(chanData);
                    }
                }
                System.out.println("Channel data reading complete.");
                return servers;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
