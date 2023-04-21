    public boolean execute(String paramString) throws SQLException {
        boolean resultFlag = false;
        int rowCount = 0;
        try {
            String sql = StringUtil.replaceString(paramString, "\\", "\\\\");
            sql = StringUtil.replaceString(sql, "\n", "\\\n");
            ;
            writer.append(sql);
            writer.append('\n');
            writer.flush();
            String result = TelnetSqliteConnection.readLine(reader);
            if (result.length() > 0 && result.charAt(0) == TelnetSqliteConnection.PREFIX_SUCCESS) {
                resultFlag = true;
                try {
                    rowCount = Integer.parseInt(result.substring(1));
                } catch (NumberFormatException e) {
                    ExceptionHandler.debug(e);
                }
            } else if (result.length() > 0 && result.charAt(0) == TelnetSqliteConnection.PREFIX_ERROR) {
                readByPrompt();
                throw new SQLException(result.substring(1));
            } else {
                throw new IOException(result);
            }
        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException e2) {
                ExceptionHandler.debug(e);
            }
            throw new SQLException(e.getMessage());
        }
        try {
            if (resultFlag) {
                this.resultSet = TelnetSqliteResultSet.createResultSet(socket, reader, writer, rowCount);
            }
            readByPrompt();
        } catch (IOException e2) {
            try {
                this.socket.close();
            } catch (IOException e3) {
                ExceptionHandler.error(e3);
            }
        }
        return resultFlag;
    }
