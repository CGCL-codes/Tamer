    public void makeFilter(long index, String name, FolderType folder, Vector smss, Vector nsms, Vector emails, SearchTerm condition) {
        DBConnection con = getConnection(false);
        try {
            long filterid = 0;
            BigDecimal folderid = null;
            try {
                folderid = storage.getFolderID(con, folder);
            } catch (FolderException err) {
                logError("Failed to get folder for filter", err);
            }
            PreparedStatement query = null;
            if (index == 0) {
                Inserter inserter = new Inserter(con, "insert into msg_Filter (user_oid, name, folderID," + " sequence, condition)" + " select ?, ?, ?, isnull(max(sequence), 0)+1, ?" + " from msg_Filter where user_oid=?", "msg_Filter");
                inserter.setBigDecimal(1, user_oid);
                inserter.setString(2, name);
                inserter.setBigDecimal(3, folderid);
                inserter.setBytes(4, filterData(condition));
                inserter.setBigDecimal(5, user_oid);
                filterid = inserter.execute();
            } else {
                query = con.prepareStatement("update msg_Filter set name=?, folderID=?, condition=?" + " where user_oid=? and ID=?");
                query.setString(1, name);
                query.setBigDecimal(2, folderid);
                query.setBytes(3, filterData(condition));
                query.setBigDecimal(4, user_oid);
                filterid = index;
                query.setLong(5, filterid);
                if (con.executeUpdate(query, null) == 0) {
                    con.rollback();
                    throw new HamboFatalException("Hacker alert...tried to update something that did not belong to the user!");
                }
                query = con.prepareStatement("delete msg_FilterRedirect where filterID=?");
                query.setLong(1, filterid);
                con.executeUpdate(query, null);
            }
            for (int i = 0; i < smss.size(); ++i) {
                query = con.prepareStatement("insert into msg_FilterRedirect" + " (filterID, type, recipient, split_max)" + " values (?,2,?,?)");
                query.setLong(1, filterid);
                query.setString(2, (String) smss.elementAt(i));
                query.setInt(3, ((Integer) nsms.elementAt(i)).intValue());
                con.executeUpdate(query, null);
            }
            for (int i = 0; i < emails.size(); ++i) {
                query = con.prepareStatement("insert into msg_FilterRedirect" + " (filterID, type, recipient, split_max)" + " values (?,1,?,0)");
                query.setLong(1, filterid);
                query.setString(2, (String) emails.elementAt(i));
                con.executeUpdate(query, null);
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (Exception err2) {
                logError("Failed to rollback after " + e, err2);
            }
            throw new HamboFatalException("Failed to create filter", e);
        } finally {
            releaseConnection(con);
        }
    }
