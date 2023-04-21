    public static String[][] getChannelData() {
        try {
            URL whatismyip = new URL("http://www.witna.co.uk/chanData/channelList.html");
            InputStream stream = whatismyip.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String line = in.readLine();
            if (line == null) {
                stream.close();
                in.close();
                return new String[0][4];
            }
            int numChannels = Integer.parseInt(line);
            String[][] channels = new String[numChannels][4];
            for (int x = 0; x < numChannels; x++) {
                String chanData = in.readLine();
                if (chanData != null && isMasterChannelDataValid(chanData)) {
                    channels[x][0] = getServerIP(chanData);
                    for (int y = 1; y < 4; y++) {
                        channels[x][y] = readQuotes(chanData, y);
                    }
                } else {
                    System.out.println(chanData);
                }
            }
            stream.close();
            in.close();
            return channels;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0][7];
        }
    }
