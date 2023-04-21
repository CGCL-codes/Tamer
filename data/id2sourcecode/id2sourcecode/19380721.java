    private void oracleSaveBlob(String fieldName, InputStream data, int dataSize) throws DataException {
        try {
            if (myConnection == null) {
                myConnection = DBConnectionPool.getInstance(getCriteria().getMappedDataContext()).getConnection("LOB Field Connection");
            }
        } catch (DBException ex) {
            throw new DataException("Error getting Database" + " Connection for BLOB Retrieval", ex);
        }
        oraclePrepUpdateEmptyLob(getCriteria(), fieldName, myConnection);
        try {
            if (data != null) {
                oraclePrepSelectForUpdate(getCriteria(), fieldName, myConnection);
                if (myConnection.next()) {
                    Class oracleResultSetClass = Class.forName("oracle.jdbc.driver.OracleResultSet");
                    Class[] parameterTypes = new Class[] { int.class };
                    Object[] arguments = new Object[] { new Integer(1) };
                    Method getBlobMethod = oracleResultSetClass.getMethod("getBlob", parameterTypes);
                    Object blob = getBlobMethod.invoke((Object) myConnection.getResultSet(), arguments);
                    parameterTypes = new Class[] {};
                    arguments = new Object[] {};
                    Class oracleBlobClass = Class.forName("oracle.sql.BLOB");
                    Method getBinaryOutputStreamMethod = oracleBlobClass.getMethod("getBinaryOutputStream", parameterTypes);
                    OutputStream oBlob = (OutputStream) getBinaryOutputStreamMethod.invoke(blob, arguments);
                    Method getChunkSizeMethod = oracleBlobClass.getMethod("getChunkSize", parameterTypes);
                    byte[] chunk = new byte[((Integer) getChunkSizeMethod.invoke(blob, arguments)).intValue()];
                    int i = -1;
                    while ((i = data.read(chunk)) != -1) oBlob.write(chunk, 0, i);
                    oBlob.close();
                    data.close();
                    myConnection.commit();
                } else {
                    throw new DataException("Error SELECTing record for update.");
                }
            }
        } catch (DBException ex) {
            throw new DataException("Error SELECTing record for update.", ex);
        } catch (NoSuchMethodException ex) {
            throw new DataException("Reflection error on oracle classes.", ex);
        } catch (IllegalAccessException ex) {
            throw new DataException("Reflection error on oracle classes.", ex);
        } catch (InvocationTargetException ex) {
            throw new DataException("Reflection error on oracle classes.", ex);
        } catch (ClassNotFoundException ex) {
            throw new DataException("Reflection error on oracle classes.", ex);
        } catch (IOException ex) {
            throw new DataException("Error reading from InputStream.", ex);
        }
    }
