    private void oracleSaveClob(String fieldName, java.io.Reader data, int length) throws DataException {
        try {
            if (myConnection == null) {
                myConnection = DBConnectionPool.getInstance(getCriteria().getMappedDataContext()).getConnection("LOB Field Connection");
            }
        } catch (DBException ex) {
            throw new DataException("Error getting Database" + " Connection for CLOB Retrieval", ex);
        }
        if (data == null) {
            oraclePrepUpdateNullClob(getCriteria(), fieldName, myConnection);
            return;
        }
        oraclePrepUpdateEmptyLob(getCriteria(), fieldName, myConnection);
        try {
            oraclePrepSelectForUpdate(getCriteria(), fieldName, myConnection);
            if (myConnection.next()) {
                Class oracleResultSetClass = Class.forName("oracle.jdbc.driver.OracleResultSet");
                Class[] parameterTypes = new Class[] { int.class };
                Object[] arguments = new Object[] { new Integer(1) };
                Method getClobMethod = oracleResultSetClass.getMethod("getClob", parameterTypes);
                Object clob = getClobMethod.invoke((Object) myConnection.getResultSet(), arguments);
                parameterTypes = new Class[] {};
                arguments = new Object[] {};
                Class oracleClobClass = Class.forName("oracle.sql.CLOB");
                Method getCharacterOutputStream = oracleClobClass.getMethod("getCharacterOutputStream", parameterTypes);
                Writer oClob = (Writer) getCharacterOutputStream.invoke(clob, arguments);
                Method getChunkSizeMethod = oracleClobClass.getMethod("getChunkSize", parameterTypes);
                char[] chunk = new char[((Integer) getChunkSizeMethod.invoke(clob, arguments)).intValue()];
                int i = -1;
                while ((i = data.read(chunk)) != -1) oClob.write(chunk, 0, i);
                oClob.close();
                data.close();
                myConnection.commit();
            } else {
                throw new DataException("Error SELECTing record for update.");
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
