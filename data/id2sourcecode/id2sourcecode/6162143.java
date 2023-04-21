    public void doFetch(String userid) {
        if (builder == null) return;
        InputStream in = null;
        try {
            URL url = new URL("http://me2day.net/" + userid + "/rss");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Java/" + System.getProperty("java.version") + " (JMSN with me2day.net)");
            in = con.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            while (true) {
                int readlen = in.read(buf);
                if (readlen < 1) break;
                bos.write(buf, 0, readlen);
            }
            String str = new String(bos.toByteArray(), "UTF-8");
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            processDocument(doc);
        } catch (Exception e) {
            System.err.println("Error on me2day.net: " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
                in = null;
            }
        }
    }
