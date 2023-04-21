    private static URLConnection handleMSRedirect(URLConnection c) throws IOException {
        DataInputStream in = new DataInputStream(c.getInputStream());
        String line;
        URL url = null;
        while ((line = in.readLine()) != null) {
            int i = line.indexOf('=');
            if (i > 0) {
                url = new URL(line.substring(i + 1).trim());
                break;
            }
        }
        in.close();
        if (url == null) {
            throw new IOException("Cannot resolve MS Redirect");
        }
        log.debug("Following redirect to " + url);
        URLConnection nc = url.openConnection();
        nc.setRequestProperty("User-Agent", "JReceiver/@version@");
        nc.setRequestProperty("Pragma", "xPlayStrm=1");
        nc.connect();
        if (nc.getContentType().equals("application/octet-stream")) {
            log.debug("Got live stream");
            return nc;
        }
        nc.getInputStream().close();
        throw new IOException("Couldn't get live data stream");
    }
