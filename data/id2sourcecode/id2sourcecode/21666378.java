    private void updateServerList() {
        String listUrl = JOptionPane.showInputDialog(this, "Enter listUrl for the server list file", serverListUrl);
        if (listUrl != null) {
            try {
                ArrayList<ServerInfo> servers = new ArrayList<ServerInfo>();
                URL url = new URL(listUrl);
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                Reader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null && !"---".equals(line)) {
                    String[] s = line.split(":| ");
                    int port = Integer.parseInt(s[1]);
                    servers.add(new ServerInfo(s[0], port));
                    line = br.readLine();
                }
                in.close();
                reader.close();
                br.close();
                serverInfos = servers;
                updateServerComboBox();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Can not load server list: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
