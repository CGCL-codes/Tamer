    public synchronized void addDocument(final Document doc) throws IOException {
        SQLException ex = null;
        try {
            this.con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            int fileId = 1;
            ResultSet rs = stmt.executeQuery("SELECT MAX( ID ) AS \"MaxID\" FROM File_");
            if (null != rs && rs.next()) {
                fileId = rs.getInt("MaxID") + 1;
            }
            rs.close();
            rs = null;
            PreparedStatement insertFileStmt = con.prepareStatement("INSERT INTO File_ ( ID, Name ) VALUES ( ?, ? )");
            insertFileStmt.setInt(1, fileId);
            insertFileStmt.setString(2, doc.getField("path").stringValue());
            int inserted = insertFileStmt.executeUpdate();
            PreparedStatement insertFieldStmt = con.prepareStatement("INSERT INTO Field_ ( ID, FileID, Name, Value, FullValue, IsIndexable, IsCompressed ) VALUES ( ?, ?, ?, ?, ?, ?, ? )");
            PreparedStatement insertTokenStmt = con.prepareStatement("INSERT INTO Token_ ( FieldID, Word_, Occurrences ) VALUES ( ?, ?, ? )");
            int fieldId = 1;
            rs = stmt.executeQuery("SELECT MAX( ID ) AS \"MaxID\" FROM Field_");
            if (null != rs && rs.next()) {
                fieldId = rs.getInt("MaxID") + 1;
            }
            rs.close();
            rs = null;
            for (Enumeration elements = doc.fields(); elements.hasMoreElements(); ) {
                final Field field = (Field) elements.nextElement();
                String value = field.stringValue();
                if (!field.isStored()) {
                    value = null;
                }
                byte[] fullValue = null == value || value.length() <= 255 ? null : value.getBytes();
                boolean isCompressed = false;
                if (null != fullValue && field.isTokenized()) {
                    final ByteArrayOutputStream newFullValue = new ByteArrayOutputStream();
                    final byte[] buf = new byte[4000];
                    final Deflater compresser = new Deflater();
                    compresser.setInput(fullValue);
                    compresser.finish();
                    while (!compresser.finished()) {
                        final int read = compresser.deflate(buf);
                        newFullValue.write(buf, 0, read);
                    }
                    compresser.end();
                    if (newFullValue.size() < fullValue.length) {
                        fullValue = newFullValue.toByteArray();
                        isCompressed = true;
                    }
                }
                insertFieldStmt.setInt(1, fieldId);
                insertFieldStmt.setInt(2, fileId);
                insertFieldStmt.setString(3, field.name());
                insertFieldStmt.setString(4, isCompressed || null == value ? null : StringUtils.left(value, 255));
                if (isCompressed || (null != fullValue && fullValue.length > 255)) {
                    insertFieldStmt.setBytes(5, fullValue);
                } else {
                    insertFieldStmt.setObject(5, null);
                }
                insertFieldStmt.setBoolean(6, field.isIndexed());
                insertFieldStmt.setBoolean(7, isCompressed);
                inserted = insertFieldStmt.executeUpdate();
                if (null != field.stringValue()) {
                    final Map<String, Integer> tokenMap = new HashMap<String, Integer>(field.isTokenized() ? field.stringValue().length() / this.AVG_WORDLENGTH : 1);
                    if (field.isTokenized()) {
                        if (!this.USE_LUCENE_TOKENIZER) {
                            for (StringTokenizer tokenizer = new StringTokenizer(field.stringValue(), " |&.:?,;!()[]/\t\n\r\f\240"); tokenizer.hasMoreTokens(); ) {
                                final String token = tokenizer.nextToken().toLowerCase();
                                if (tokenMap.containsKey(token)) {
                                    tokenMap.put(token, tokenMap.get(token) + 1);
                                } else {
                                    tokenMap.put(token, 1);
                                }
                            }
                        } else {
                            final Tokenizer tokenizer = new StandardTokenizer(new StringReader(field.stringValue()));
                            Token token = null;
                            while ((token = tokenizer.next()) != null) {
                                final String tokenText = token.termText().toLowerCase();
                                if (tokenMap.containsKey(tokenText)) {
                                    tokenMap.put(tokenText, tokenMap.get(tokenText) + 1);
                                } else {
                                    tokenMap.put(tokenText, 1);
                                }
                            }
                        }
                    } else {
                        tokenMap.put(field.stringValue().toLowerCase(), 1);
                    }
                    for (String token : tokenMap.keySet()) {
                        insertTokenStmt.setInt(1, fieldId);
                        insertTokenStmt.setString(2, token.substring(0, Math.min(token.length(), 255)));
                        insertTokenStmt.setInt(3, tokenMap.get(token));
                        try {
                            insertTokenStmt.executeUpdate();
                        } catch (SQLException e) {
                        }
                    }
                }
                ++fieldId;
            }
            this.con.commit();
            insertTokenStmt.close();
            insertTokenStmt = null;
            insertFieldStmt.close();
            insertFieldStmt = null;
            insertFileStmt.close();
            insertFileStmt = null;
            stmt.close();
            stmt = null;
        } catch (SQLException e) {
            e.printStackTrace();
            ex = e;
            try {
                this.con.rollback();
            } catch (SQLException e2) {
            }
        } finally {
            try {
                this.con.setAutoCommit(true);
            } catch (SQLException e2) {
            }
        }
        if (null != ex) throw new IOException(ex.getMessage());
    }
