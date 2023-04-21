    @Override
    public List<UpdateResponse> select(Connection conn, Date lastUpdate, String applicationId) throws SQLException, DatabaseException, RTServerException, Exception {
        ArrayList<UpdateResponse> urs = new ArrayList<UpdateResponse>();
        XmlOptions parseOptions = getDbParseOptions();
        Statement s = conn.createStatement();
        String modDate = AbsDAO.dbDateFormat.format(lastUpdate);
        String sql = "SELECT xml, file_content, db_deleted, db_created FROM " + StringEscapeUtils.escapeSql(this.getTableName()) + " " + "WHERE application_id = '" + StringEscapeUtils.escapeSql(applicationId) + "' " + "AND db_modified > '" + StringEscapeUtils.escapeSql(modDate) + "'";
        ResultSet rs = s.executeQuery(sql);
        urs.ensureCapacity(rs.getFetchSize());
        FileType item = null;
        while (rs.next()) {
            int i = 1;
            item = FileType.Factory.parse(rs.getString(i++), parseOptions);
            Blob fileBlob = rs.getBlob(i++);
            File file = File.createTempFile("tmp.rt.server.FileType.file", "", new File(RTClientProperties.instance().getTempDir()));
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileBlob.getBytes(1, (int) fileBlob.length()));
            fos.close();
            boolean isDeleted = rs.getBoolean(i++);
            java.sql.Timestamp createDate = rs.getTimestamp(i++);
            UpdateResponseCode urCode;
            if (isDeleted) {
                urCode = UpdateResponseCode.deletedItem;
            } else if (createDate.getTime() > lastUpdate.getTime()) {
                urCode = UpdateResponseCode.newItem;
            } else {
                urCode = UpdateResponseCode.modifiedItem;
            }
            FileUpdateResponse ur = new FileUpdateResponse(urCode, item);
            ur.setResponeFile(file);
            urs.add(ur);
        }
        s.close();
        return urs;
    }
