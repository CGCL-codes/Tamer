    public void save() {
        Connection connection = null;
        ZipOutputStream zip = null;
        try {
            connection = dataSource.getConnection();
            IVirtualFile storage = BackupUtil.rotate(this.storageDirectory, "backup", "dat", 20);
            storage.setIOHandler(new EncryptionIOHandler(provider));
            zip = new ZipOutputStream(storage.getOutputStream());
            PrintWriter writer = new PrintWriter(zip, true);
            for (EncryptedTable table : getEncryptedTables(connection)) {
                ZipEntry entry = new ZipEntry(table.tableName + ".dat");
                zip.putNextEntry(entry);
                table.save(connection, writer);
                writer.flush();
                zip.closeEntry();
            }
            zip.flush();
            zip.close();
        } catch (Exception e) {
            if (zip != null) {
                try {
                    zip.close();
                    BackupUtil.reverseRotation(this.storageDirectory, "backup", "dat", 20);
                } catch (Exception ee) {
                    throw ThrowableManagerRegistry.caught(ee);
                }
            }
        } finally {
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                ThrowableManagerRegistry.caught(e);
            }
        }
    }
