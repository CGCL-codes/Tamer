    InputStream selectSource(String item) {
        if (item.endsWith(".pls")) {
            item = fetchPls(item);
            if (item == null) {
                return null;
            }
        } else if (item.endsWith(".m3u")) {
            item = fetchM3u(item);
            if (item == null) {
                return null;
            }
        }
        if (!item.endsWith(".ogg")) {
            return null;
        }
        InputStream is = null;
        URLConnection urlc = null;
        try {
            URL url = null;
            if (runningAsApplet) {
                url = new URL(getCodeBase(), item);
            } else {
                url = new URL(item);
            }
            urlc = url.openConnection();
            is = urlc.getInputStream();
            currentSource = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getFile();
        } catch (Exception ee) {
            System.err.println(ee);
        }
        if (is == null && !runningAsApplet) {
            try {
                is = new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + item);
                currentSource = null;
            } catch (Exception ee) {
                System.err.println(ee);
            }
        }
        if (is == null) {
            return null;
        }
        System.out.println("Select: " + item);
        boolean find = false;
        for (int i = 0; i < cb.getItemCount(); i++) {
            String foo = (String) (cb.getItemAt(i));
            if (item.equals(foo)) {
                find = true;
                break;
            }
        }
        if (!find) {
            cb.addItem(item);
        }
        int i = 0;
        String s = null;
        String t = null;
        udpPort = -1;
        udpBaddress = null;
        while (urlc != null) {
            s = urlc.getHeaderField(i);
            t = urlc.getHeaderFieldKey(i);
            if (s == null) {
                break;
            }
            i++;
            if (t != null && t.equals("udp-port")) {
                try {
                    udpPort = Integer.parseInt(s);
                } catch (Exception ee) {
                    System.err.println(ee);
                }
            } else if (t != null && t.equals("udp-broadcast-address")) {
                udpBaddress = s;
            }
        }
        return is;
    }
