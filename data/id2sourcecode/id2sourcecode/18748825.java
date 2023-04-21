    public void write() throws WriterException {
        Connection connection = null;
        Statement stmt = null;
        try {
            init();
            if (jdbcJar != null) {
                connection = DBUtils.openConnection(jdbcJar, driver, url, username, password);
            } else {
                connection = DBUtils.openConnection(driver, url, username, password);
            }
            stmt = connection.createStatement();
            if (commit) {
                connection.setAutoCommit(false);
            }
            for (int i = 0; i < getStorage().size(); i++) {
                String line = (String) getStorage().get(i);
                stmt.executeUpdate(line.substring(0, line.length() - 1));
            }
            if (commit) {
                connection.commit();
            }
        } catch (StorageException e) {
            throw new WriterException("cannot read data from temporary storage", e);
        } catch (SQLException e) {
            throw new WriterException("cannot write statement to database", e);
        } catch (IOException e) {
            throw new WriterException("cannot find database driver", e);
        } catch (ClassNotFoundException e) {
            throw new WriterException("cannot load database driver", e);
        } finally {
            DBUtils.closeConnection(connection);
        }
    }
