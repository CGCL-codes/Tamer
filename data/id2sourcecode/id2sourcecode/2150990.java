    public int executeScript(BufferedReader reader, Writer writer) throws SQLException, IOException {
        setWriter(writer);
        if (!reader.ready()) return 0;
        if (triggerLanguage != null) procedures = new Vector();
        boolean wasAutoCommit = false;
        if (writer == null) {
            wasAutoCommit = connection.getAutoCommit();
            if (wasAutoCommit) connection.setAutoCommit(false); else connection.commit();
            if (explicitCommitStatement != null) {
                statement.executeUpdate(explicitCommitStatement);
            }
        }
        String current;
        Vector block = new Vector();
        current = reader.readLine();
        int i = 0;
        try {
            do {
                if ((current.length() == 0) || (current.charAt(0) == comment)) {
                    if ((writer != null) && (current.length() > 0) && (commentStartsWith != null)) {
                        writer.write(commentStartsWith + current.substring(1) + statementTerminator);
                    }
                    current = reader.readLine();
                    continue;
                }
                block.addElement(current.substring(1));
                current = executeBlock(reader, current.charAt(0), block);
                i++;
                block.removeAllElements();
            } while (reader.ready());
            if ((current != null) && (current.length() != 0) && (current.charAt(0) != comment)) {
                block.addElement(current.substring(1));
                executeBlock(reader, current.charAt(0), block);
                i++;
            }
        } catch (Exception e) {
            if (writer == null) {
                connection.rollback();
                if (wasAutoCommit) connection.setAutoCommit(true);
            }
            System.out.println(current);
            if (e instanceof SQLException) throw (SQLException) e; else if (e instanceof IOException) throw (IOException) e; else {
                e.printStackTrace();
                throw new SQLException("Undefined exception");
            }
        }
        connection.commit();
        if (wasAutoCommit) connection.setAutoCommit(true);
        return i;
    }
