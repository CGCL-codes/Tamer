    private String downloadFeed(String feedUrl2) {
        String data = "";
        System.out.print("\t[" + feedUrl2 + "]");
        try {
            final URL url = new URL(feedUrl2);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            long l = 0;
            long len = 0;
            while ((l = in.read(buffer)) != -1) {
                len += l;
                data += new String(buffer);
            }
            System.out.print("\t[ " + (len / 1024) + " kB ]");
        } catch (Exception e) {
            System.err.println("\n\t" + e.getMessage());
        }
        String dataClean = "";
        final String start = "<description>";
        final String end = "</description>";
        data = data.toLowerCase();
        int startpos = data.indexOf(start);
        int c = 0;
        while (startpos != -1) {
            int endpos = data.indexOf(end, startpos);
            if (endpos == -1) break;
            c++;
            dataClean += data.substring(startpos + start.length(), endpos) + " ";
            startpos = data.indexOf(start, endpos);
        }
        System.out.print("\t[ " + (c) + " stories ]");
        sc = c;
        System.out.println("");
        return dataClean;
    }
