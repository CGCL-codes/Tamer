    public boolean loadData(URL url, String s) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object obj = null;
        String s2 = null;
        String s3 = "";
        try {
            int q = main.HOST.indexOf('q');
            String day = main.app.getParameter("day");
            s = main.HOST.substring(0, q - 1) + "files/chess/data/" + day + ".txt";
            URL url1 = new URL(s);
            java.io.StringBufferInputStream sbis = new java.io.StringBufferInputStream(s);
            DataInputStream datainputstream = new DataInputStream(url1.openStream());
            String s1;
            while ((s1 = datainputstream.readLine()) != null) if (!s1.startsWith("*") && s1.trim().length() != 0) if (s2 == null) s2 = new String(s1); else s3 += s1 + " ";
            datainputstream.close();
            parseFEN(new StringTokenizer(s2));
            parseSAN(new StringTokenizer(s3));
            return true;
        } catch (Exception exception) {
            System.err.println("Input File Error: " + exception);
            System.err.println("url: " + s);
        }
        return false;
    }
