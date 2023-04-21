    private Collection<Document> getDocuments(final Connection con, final Set<Integer> fileIDs) throws SQLException, DataFormatException, IOException, ClassNotFoundException {
        final Map<Integer, Document> result = new HashMap<Integer, Document>(fileIDs.size());
        final Set<Integer> fileIDsCopy = new HashSet<Integer>(fileIDs);
        if (DEBUG) System.err.println(this.getClass().getName() + ": Starting document retrieval at " + new java.util.Date());
        while (fileIDsCopy.size() > 0) {
            Set<Integer> max1000FileIDs = new HashSet<Integer>(new ArrayList<Integer>(fileIDsCopy).subList(0, Math.min(fileIDsCopy.size(), 1000)));
            fileIDsCopy.removeAll(max1000FileIDs);
            final String in = "?" + StringUtils.repeat(",?", max1000FileIDs.size() - 1);
            if (DEBUG) System.err.println(this.getClass().getName() + ": IDs: " + max1000FileIDs.size());
            PreparedStatement getFilesStmt = con.prepareStatement("SELECT ID AS \"FileID\", Name AS \"Name\" FROM File_ WHERE ID IN ( " + in + " )");
            PreparedStatement getFieldsStmt = con.prepareStatement("SELECT FileID AS \"FileID\", ID AS \"FieldID\", Name AS \"Name\", Value AS \"Value\", FullValue AS \"FullValue\", IsCompressed AS \"IsCompressed\" FROM Field_ WHERE FileID IN ( " + in + " )");
            PreparedStatement getTokensStmt = con.prepareStatement("SELECT FileID AS \"FileID\", Word_ AS \"Word\", Occurrences AS \"Occurrences\" FROM Token_ T INNER JOIN Field_ F ON T.FieldID = F.ID WHERE F.FileID IN ( " + in + " )");
            int i = 1;
            for (int fileID : max1000FileIDs) {
                getFilesStmt.setInt(i, fileID);
                getFieldsStmt.setInt(i, fileID);
                getTokensStmt.setInt(i, fileID);
                ++i;
            }
            if (DEBUG) System.err.println(this.getClass().getName() + ": Getting file data at " + new java.util.Date());
            ResultSet rs = getFilesStmt.executeQuery();
            while (rs.next()) {
                int fileID = rs.getInt("FileID");
                Document doc = new Document();
                result.put(fileID, doc);
            }
            rs.close();
            if (DEBUG) System.err.println(this.getClass().getName() + ": Getting field data at " + new java.util.Date());
            rs = getFieldsStmt.executeQuery();
            while (rs.next()) {
                final int fileID = rs.getInt("FileID");
                final int fieldID = rs.getInt("FieldID");
                final String name = rs.getString("Name");
                String value = rs.getString("Value");
                final byte[] fullValue = rs.getBytes("FullValue");
                final boolean isCompressed = rs.getBoolean("IsCompressed");
                if (isCompressed) {
                    final ByteArrayOutputStream newFullValue = new ByteArrayOutputStream();
                    final Inflater decompresser = new Inflater();
                    decompresser.setInput(fullValue);
                    final byte[] buf = new byte[4000];
                    while (!decompresser.finished()) {
                        final int read = decompresser.inflate(buf);
                        newFullValue.write(buf, 0, read);
                    }
                    decompresser.end();
                    value = newFullValue.toString();
                }
                final Document doc = result.get(fileID);
                if (null != value) {
                    doc.add(new Field(name, value, Field.Store.NO, Field.Index.NO));
                }
            }
            rs.close();
            if (DEBUG) System.err.println(this.getClass().getName() + ": Getting token data at " + new java.util.Date());
            rs = getTokensStmt.executeQuery();
            while (rs.next()) {
                final int fileID = rs.getInt("FileID");
                final String word = rs.getString("Word");
                final int occurrences = rs.getInt("Occurrences");
                final Document doc = result.get(fileID);
                final Field field = new Field("token", word, Field.Store.NO, Field.Index.NO);
                field.setBoost(occurrences);
                doc.add(field);
            }
            rs.close();
            rs = null;
            getTokensStmt.close();
            getTokensStmt = null;
            getFieldsStmt.close();
            getFieldsStmt = null;
            getFilesStmt.close();
            getFilesStmt = null;
            if (DEBUG) System.err.println(this.getClass().getName() + ": " + result.size() + " Documents ready at " + new java.util.Date());
        }
        return result.values();
    }
