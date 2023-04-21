    public static String computeSignature(URL url) {
        try {
            URLConnection con = url.openConnection();
            if (con == null) return "(can't open connection)";
            con.connect();
            if (con instanceof HttpURLConnection) {
                HttpURLConnection hcon = (HttpURLConnection) con;
                int code = hcon.getResponseCode();
                if (code != 200) return "(bad connection " + code + ")";
                if (StudyOut != null) {
                    long lastmod = hcon.getLastModified();
                    if (lastmod > 0) StudyOut.print("m" + lastmod + " ");
                }
                HTML html = new HTML();
                try {
                    html.docURI = new URI(url.toExternalForm());
                } catch (URISyntaxException canthappen) {
                }
                Document docroot = new Document("doc", null, null);
                if (Verbose) System.out.println("Reading " + url);
                html.setInputStream(new BufferedInputStream(con.getInputStream()));
                html.parse(docroot);
                String sig = computeSignature(docroot, url);
                assert sig != null : "signatures should return error message rather than null";
                return sig;
            } else System.err.println("problem computing signature");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }
