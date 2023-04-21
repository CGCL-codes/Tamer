    public void parse(ParserEvent event) {
        logger.debug("parse() called for link " + event.getLink().getURI());
        String link = event.getLink().getURI();
        if (link.matches("\\S+&\\w{3};\\S+")) {
            link = StringEscapeUtils.unescapeHtml(event.getLink().getURI());
        }
        if (event.getCrawler().equals(crawler) && isContentLink(link)) {
            logger.debug("Content " + link + " matched");
            Statement stmt = null;
            ResultSet rs = null;
            long checksum1 = 0;
            try {
                URL url = new URL(link);
                checksum1 = url.openConnection().getLastModified();
            } catch (MalformedURLException mfe) {
                logger.error("Malformed url " + link + " - " + mfe.getMessage());
                return;
            } catch (IOException ioe) {
                logger.error("Couldn't read " + link + " - " + ioe.getMessage());
                return;
            }
            if ((checksum1 == 0) || (checksum1 == 1)) {
                Adler32 adler32 = new Adler32();
                NodeList nodes = (NodeList) event.getPageData().getData();
                adler32.update(nodes.toHtml().getBytes());
                checksum1 = adler32.getValue();
            }
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM documents WHERE url='" + link + "'");
                if (rs.first()) {
                    long checksum2 = rs.getLong("checksum");
                    if (checksum1 == checksum2) {
                        stmt.executeUpdate("UPDATE documents SET checked=" + String.valueOf(System.currentTimeMillis()) + " WHERE id=" + rs.getString("id"));
                    } else {
                        this.updateContent(getContent(link));
                        stmt.executeUpdate("UPDATE documents SET checksum=" + checksum1 + ", checked=" + String.valueOf(System.currentTimeMillis()) + " WHERE id=" + rs.getString("id"));
                    }
                } else {
                    this.addContent(getContent(link));
                    stmt.executeUpdate("INSERT INTO documents VALUES " + "(DEFAULT, '" + link + "', " + checksum1 + ", '" + getId() + "', " + String.valueOf(System.currentTimeMillis()) + ")");
                }
            } catch (SQLException e) {
                logger.error("Could't perform database query or update - " + e.getMessage());
            } catch (IQserException e) {
                logger.error("Couldn't perform repository update - " + e.getLocalizedMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException sqlEx) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) {
                    }
                    stmt = null;
                }
            }
        }
    }
