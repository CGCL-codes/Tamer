    public static Hashtable getTerrainList(URL url) {
        Hashtable table = new Hashtable();
        String document = null;
        try {
            InputStream is = url.openStream();
            byte[] buffer = new byte[10240];
            int count = 0;
            while (is.available() > 0) {
                count += is.read(buffer, count, buffer.length - count);
            }
            String s = new String(buffer);
            int idx = 0;
            while ((idx = s.indexOf("<A HREF=", idx)) > 0) {
                int start = s.indexOf("\"", idx);
                int end = s.indexOf("\"", start + 1);
                String name = s.substring(start + 1, end - 1);
                if (name.startsWith("?") || name.startsWith("/")) {
                } else {
                    System.out.println(name);
                    URL infoUrl = new URL(url.toString() + "/" + name);
                    MapXML map = getTerrainInfo(infoUrl);
                    table.put(name, map);
                }
                idx = end;
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }
