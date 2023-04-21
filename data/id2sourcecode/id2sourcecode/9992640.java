    protected void insertDocOrURL(long quakeid, OrdHttpUploadFile doc, URL url, int type, String description, String info) {
        Connection orclConn = null;
        try {
            orclConn = getOracleConnection();
            orclConn.setAutoCommit(false);
            OraclePreparedStatement stmt = (OraclePreparedStatement) orclConn.prepareStatement(INSERT_DOC);
            stmt.setLong(1, quakeid);
            stmt.setString(2, description);
            stmt.setString(3, info);
            stmt.executeUpdate();
            Debug.output("DocumentDAO:" + quakeid + "  " + description + "  " + info);
            stmt = (OraclePreparedStatement) orclConn.prepareStatement(CURRVAL);
            OracleResultSet rset = (OracleResultSet) stmt.executeQuery();
            if (!rset.next()) {
                throw new SeismoException("CURRVAL not found !!!");
            }
            long docid = rset.getLong(1);
            System.out.println("docid : " + docid);
            stmt = (OraclePreparedStatement) orclConn.prepareStatement(FETCH_DOC_FOR_UPDATE);
            stmt.setLong(1, docid);
            Debug.output("DocumentDAO:" + docid);
            rset = (OracleResultSet) stmt.executeQuery();
            if (!rset.next()) {
                throw new SeismoException("New row not found in table");
            }
            OrdDoc document = (OrdDoc) rset.getCustomDatum(1, OrdDoc.getFactory());
            if (type == DOC && doc != null) {
                loadDoc(document, doc);
            } else if (type == URL && url != null) {
                loadURL(document, url);
            } else {
                throw new SeismoException("Wrong document type. Expected DOC or URL.");
            }
            stmt = (OraclePreparedStatement) orclConn.prepareStatement(UPDATE_DOC);
            stmt.setCustomDatum(1, document);
            stmt.setLong(2, docid);
            stmt.execute();
            stmt.close();
            Debug.output("DOC format: " + document.getFormat());
            Debug.output("DOC MIME: " + document.getMimeType());
            Debug.output("DOC source: " + document.getSource());
            orclConn.commit();
        } catch (SQLException e) {
            try {
                orclConn.rollback();
            } catch (SQLException ex) {
                throw new SeismoException(ex);
            }
        } catch (IOException e) {
            throw new SeismoException(e);
        } finally {
            freeOracleConnection(orclConn);
        }
    }
