    private static String request(URL url) throws IOException {
        StringWriter content = null;
        try {
            content = new StringWriter();
            BufferedReader in = null;
            try {
                URLConnection cnx = url.openConnection();
                cnx.setRequestProperty("User-Agent", "Mozilla/5.25 Netscape/5.0 (Windows; I; Win95)");
                in = new BufferedReader(new InputStreamReader(cnx.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    content.write(line);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            return content.toString();
        } finally {
            if (content != null) {
                content.close();
            }
        }
    }
