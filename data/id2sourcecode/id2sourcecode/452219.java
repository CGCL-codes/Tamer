    private String checkXMLFile(Node node, String identifier, String ds, String status, boolean valid) {
        Node newNode;
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String xmlFilename = "";
        java.util.Date datestamp = new java.util.Date();
        String strDatestamp = "";
        int i = 0;
        if (includeRecord(node) == false) {
            return identifier;
        }
        normalizeRecord(node);
        newNode = normalizeXML(node);
        total++;
        subtotal++;
        xmlFilename = createFileName(identifier);
        if (valid == false) {
            xmlFilename += ".not";
        }
        if (ds != null) {
            try {
                datestamp = formatter1.parse(ds);
                strDatestamp = ds;
            } catch (ParseException pe) {
                strDatestamp = formatter1.format(new java.util.Date());
            }
        } else {
            strDatestamp = formatter1.format(datestamp);
        }
        if (status.equals("deleted")) {
            xmlFilename += ".del";
        }
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rst = stmt.executeQuery("Select * from OAIRecord where repositoryID=" + hInfo.getRepositoryID() + " And identifier='" + identifier + "';");
            String sql;
            boolean saveFile = false;
            int recordID = 0;
            if (rst.next() == false) {
                rst.close();
                sql = "Insert into OAIRecord ";
                sql += "(repositoryID, identifier, filename, harvestTime,";
                sql += "datestamp, status, metadataPrefix, updateFlag) values(";
                sql += hInfo.getRepositoryID() + ",";
                sql += "'" + identifier + "',";
                sql += "'" + xmlFilename + "',";
                sql += "'" + formatter2.format(new java.util.Date()) + "',";
                sql += "'" + strDatestamp + "',";
                sql += "'" + status + "',";
                sql += "'" + hInfo.getMetadataPrefix() + "',";
                sql += FLAG_RECORD_NEW + ");";
                stmt.executeUpdate(sql);
                rst = stmt.executeQuery("Select recordID from OAIRecord where repositoryID=" + hInfo.getRepositoryID() + " And identifier='" + identifier + "';");
                rst.next();
                recordID = rst.getInt("recordID");
                saveFile = true;
                rst.close();
            } else {
                recordID = rst.getInt("recordID");
                String rstFilename = fixNull(rst.getString("filename"));
                java.sql.Date rstDatestamp = rst.getDate("datestamp");
                String rstStatus = rst.getString("status");
                rst.close();
                sql = "Update OAIRecord Set ";
                if (!rstDatestamp.equals(datestamp) || !fixPendingStatus(rstStatus).equals(fixNull(status))) {
                    sql += "harvestTime = '" + formatter2.format(new java.util.Date()) + "', ";
                    sql += "datestamp = '" + strDatestamp + "', ";
                    sql += "status = '" + status + "', ";
                    sql += "updateFlag = " + FLAG_RECORD_MODIFIED;
                    File file = new File(rstFilename);
                    if (fixNull(status).equals("deleted") && !rstFilename.endsWith(".del")) {
                        file.renameTo(new File(rstFilename + ".del"));
                        sql += ", filename = '" + rstFilename + ".del" + "'";
                    } else if (!fixNull(status).equals("deleted") && rstFilename.endsWith(".del")) {
                        file.renameTo(new File(rstFilename.substring(0, rstFilename.length() - 4)));
                        sql += ", filename = '" + rstFilename.substring(0, rstFilename.length() - 4) + "'";
                        saveFile = true;
                        xmlFilename = rstFilename.substring(0, rstFilename.length() - 4);
                    } else {
                        saveFile = true;
                        xmlFilename = rstFilename;
                    }
                } else {
                    sql += "status = '" + status + "', ";
                    sql += "updateFlag = " + FLAG_RECORD_OLD;
                }
                sql += " Where recordID = " + recordID + ";";
                stmt.executeUpdate(sql);
            }
            if (hInfo.getSetSpec().length() > 0) {
                rst = stmt.executeQuery("Select * from OAISetMapping where recordID=" + recordID + " And setSpec='" + hInfo.getSetSpec() + "';");
                if (rst.next() == false) {
                    stmt.executeUpdate("Insert Into OAISetMapping (recordID, setSpec) Values(" + recordID + ", '" + hInfo.getSetSpec() + "');");
                }
                rst.close();
            }
            if (saveFile) {
                try {
                    File xmlFile = new File(xmlFilename);
                    if (xmlFile.exists() == false) {
                        boolean ret = xmlFile.createNewFile();
                    }
                    Serializer serializer = SerializerFactory.getSerializer(OutputProperties.getDefaultMethodProperties("xml"));
                    serializer.setOutputStream(new FileOutputStream(xmlFile));
                    serializer.asDOMSerializer().serialize(newNode);
                    xmlFile = null;
                } catch (IOException ie) {
                    writeHistory(STATUS_FAILED, "Failed to write to file.  Reason:  " + ie.getMessage());
                }
            }
            stmt.close();
        } catch (SQLException se) {
            writeHistory(STATUS_FAILED, "Error: SQLState: " + se.getMessage());
        }
        return xmlFilename;
    }
