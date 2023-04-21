    public void checkUpdate() {
        try {
            URL url = new URL(getUpdateUrl());
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            int contentLength = stream.available();
            byte[] buffer = new byte[contentLength];
            int bytesread = 0;
            int offset = 0;
            bytesread = stream.read(buffer, offset, contentLength);
            offset += bytesread;
            String newVersion = new String(buffer);
            if (!newVersion.equalsIgnoreCase("version:" + Global.version)) {
                global.getLogger().log(Level.INFO, "New version available!!!");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
