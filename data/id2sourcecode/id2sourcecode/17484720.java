    public void Message(ClientConnector cc, Map<String, String> attributes) throws Exception {
        log.debug("Starting HTTP POST Message");
        String DESTNODE = attributes.get("dest_name");
        int DESTPORT;
        int DESTPORTDEFAULT = 80;
        String destPortString = attributes.get("dest_port");
        if (destPortString != null) {
            try {
                DESTPORT = Integer.parseInt(destPortString);
            } catch (Exception e) {
                DESTPORT = DESTPORTDEFAULT;
                log.warn("Destination Port \"" + destPortString + "\" was not valid. Using default value");
            }
        } else {
            List DESTPORTLIST = axt.db.GeneralDAO.getNodeValue(DESTNODE, "msg_post_port");
            if ((DESTPORTLIST.size() > 0)) {
                try {
                    DESTPORT = Integer.parseInt(DESTPORTLIST.get(0).toString());
                } catch (Exception e) {
                    DESTPORT = DESTPORTDEFAULT;
                    log.warn("DB Destination Port \"" + destPortString + "\" was not valid. Using default value");
                }
            } else {
                DESTPORT = DESTPORTDEFAULT;
            }
        }
        String URLPATH = attributes.get("dest_post_form");
        if ((URLPATH == null) || (URLPATH.equals(""))) {
            List DESTURLPATH = axt.db.GeneralDAO.getNodeValue(DESTNODE, "msg_post_form");
            if ((DESTURLPATH.size() > 0)) {
                URLPATH = DESTURLPATH.get(0).toString();
            } else {
                URLPATH = "message.cgi";
            }
        }
        String postData = "msg_node=" + URLEncoder.encode(attributes.get("src_name"), "UTF-8");
        Iterator attrIterator = attributes.keySet().iterator();
        while (attrIterator.hasNext()) {
            String keyName = (String) attrIterator.next();
            if (keyName.toLowerCase().startsWith("msg_")) {
                postData += "&" + URLEncoder.encode(keyName, "UTF-8") + "=" + URLEncoder.encode(attributes.get(keyName), "UTF-8");
            }
        }
        URL url = new URL("http://" + DESTNODE + ":" + DESTPORT + "/" + URLPATH);
        log.debug("Full POST URL is: http://" + DESTNODE + ":" + DESTPORT + "/" + URLPATH);
        log.debug("Full POST DATA is: " + postData);
        log.debug("Connecting");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(postData);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String logResponse = "-----------------\nHTTP Post Response:\n-----------------\n";
        while ((line = rd.readLine()) != null) {
            log.debug("Received: " + line);
            logResponse += line + "\n";
        }
        log.debug("Closing");
        wr.close();
        rd.close();
        logResponse += "-----------------\n";
        cc.log(logResponse);
    }
