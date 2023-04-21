    private boolean sendRequest(Command p_command, ServerInfo p_serverInfo) {
        try {
            StringBuilder request = new StringBuilder();
            request.append(url).append("/").append(serverServlet);
            request.append("?command=").append(p_command.toString());
            request.append("&name=").append(URLEncoder.encode(p_serverInfo.name, charset));
            request.append("&port=").append(p_serverInfo.port);
            if (p_serverInfo.ip != null) {
                request.append("&ip=").append(p_serverInfo.ip);
            }
            request.append("&nbPlayers=").append(p_serverInfo.nbPlayers);
            URL objUrl = new URL(request.toString());
            URLConnection urlConnect = objUrl.openConnection();
            InputStream in = urlConnect.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            int result = reader.read();
            in.close();
            return result == 48;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
