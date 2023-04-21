    private boolean pingpong() {
        InputStream is = null;
        boolean ret = false;
        int id;
        String temp;
        try {
            if ((temp = config.get("id")) != null) {
                id = Integer.parseInt(temp);
            } else {
                id = 0;
            }
        } catch (NumberFormatException ex) {
            id = 0;
        }
        try {
            URL url = new URL(config.get("pingURL") + "?id=" + id + "&port=" + config.get("port"));
            is = url.openStream();
            String ss = "";
            byte[] s = new byte[256];
            int i = 0;
            while ((i = is.read(s, 0, 256)) >= 0) {
                ss += new String(s, 0, i);
            }
            if (ss.startsWith("Welcome to TurtleEggs")) {
                ret = true;
                if (id == 0) {
                    i = ss.indexOf("new id: ");
                    int j = ss.indexOf("\n", i + 1);
                    id = Integer.parseInt(ss.substring(i + 8, j));
                    config.put("id", Integer.toString(id));
                }
            } else {
                logger.log(Level.WARNING, "Ping-Pong Failed: " + ss);
            }
            is.close();
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "ERROR: check pingURL in config.txt");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "check internet connection");
        }
        return ret;
    }
