    public static Map<String, List<String>> getCurrentSongForMP3Stream(URL url) {
        Map<String, List<String>> header = null;
        HashMap<String, List<String>> meta = new HashMap<String, List<String>>();
        int maxLen = 24000;
        byte[] data = null;
        String song = "Unknown - Unknown";
        try {
            URLConnection connection = (URLConnection) url.openConnection();
            connection.setRequestProperty("Icy-MetaData", "1");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("User-Agent", "Winamp/5.52");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(7000);
            connection.connect();
            header = connection.getHeaderFields();
            meta.putAll(header);
            if (header.get("icy-metaint") != null) {
                maxLen = ValueHelper.asInt(header.get("icy-metaint").get(0)) + 512;
            }
            data = FileHelper.loadURLToBuffer(connection, maxLen);
            String sData = new String(data);
            int begin = sData.indexOf("StreamTitle");
            int end = sData.indexOf(";", begin);
            if (begin >= 0 && end >= 0) {
                song = sData.substring(begin + 13, end - 1);
            }
            ArrayList<String> streamTitle = new ArrayList<String>();
            streamTitle.add(song);
            meta.put("StreamTitle", streamTitle);
        } catch (IOException e) {
            Log.writeToStdout(Log.WARNING, "FileHelper", "getCurrentSongForMP3Server", e.getClass() + " " + e.getMessage());
        } catch (Exception e) {
            Log.writeToStdout(Log.WARNING, "FileHelper", "getCurrentSongForMP3Server", e.getClass() + " " + e.getMessage());
        }
        return meta;
    }
