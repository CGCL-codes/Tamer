    protected void onServerResponse(int code, String response) {
        if (code == 311 || code == 338) {
            Pattern p = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
            Matcher m = p.matcher(response);
            String ip = "";
            if (m.find()) {
                ip = m.group();
                String user = response.split(" ")[1];
                if (!ip.equals("207.192.75.252")) {
                    this.onMessage(this.getChannels()[0], "", "", "", ".ipseeker " + user + " " + ip);
                }
            }
        }
    }
