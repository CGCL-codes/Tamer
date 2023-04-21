    private static String request(URL url) throws IOException {
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            BufferedReader in = null;
            try {
                URLConnection con = url.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.25 Netscape/5.0 (Windows; I; Win95)");
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    sw.write(line);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            return sw.toString();
        } finally {
            if (sw != null) sw.close();
        }
    }
