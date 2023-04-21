    public String getMyIp() {
        String ip = "";
        if (count <= 0) count = timeout;
        if (count == timeout) {
            URL url;
            try {
                url = new URL(config.getServerPhp() + "/" + myip);
                BufferedReader data = new BufferedReader(new InputStreamReader(url.openStream()));
                last_ip = ip = data.readLine();
            } catch (IOException e) {
                last_ip = "127.0.0.1";
                e.printStackTrace();
            }
        } else {
            ip = last_ip;
        }
        count--;
        debug.print("My IP: " + ip);
        return ip;
    }
