    public int insertWebPage(String pageTitle, String pageURL, Date firstAccess, Date lastAccess, String pageSource, boolean published) {
        int pub = 0;
        int docId = -1;
        if (published) {
            pub = 1;
        }
        java.sql.Timestamp fAccess = new Timestamp(firstAccess.getTime());
        java.sql.Timestamp LAccess = new Timestamp(lastAccess.getTime());
        try {
            MessageDigest hashGenerator = MessageDigest.getInstance("SHA1");
            byte[] theDigest = hashGenerator.digest(pageSource.getBytes("UTF-8"));
            pageTitle = pageTitle.replace("'", "\\'");
            pageTitle = pageTitle.replace("\"", "\\\"");
            pageSource = pageSource.replace("'", "\\'");
            pageSource = pageSource.replace("\"", "\\\"");
            String sql = "insert into webpage(pageTitle, pageURL, firstAccess, lastAccess, pageSource, SHA1, published) values ('" + pageTitle + "','" + pageURL + "','" + fAccess + "','" + LAccess + "','" + pageSource + "','" + theDigest + "'," + pub + " )";
            logger.info(sql);
            PreparedStatement ps = dbAccess.prepareStatement(sql);
        } catch (NoSuchAlgorithmException ex) {
            logger.info(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.info(ex);
        } finally {
        }
        return docId;
    }
