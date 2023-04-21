    protected void addChangedFiles(String hash, String repoID, List<String> changedFiles) throws Exception {
        if (changedFiles == null) {
            return;
        }
        Connection conn = null;
        PreparedStatement filesStmt = null;
        PreparedStatement changedFilesStmt = null;
        try {
            conn = getConnection();
            filesStmt = conn.prepareStatement("insert ignore into files (filehash, path) values (?, ?)");
            changedFilesStmt = conn.prepareStatement("insert ignore into changedfiles (hash, filehash, repoid) values (?, ?, ?)");
            for (String changedFile : changedFiles) {
                String fileHash = new String(hex.encode(messageDigestSHA1.digest(changedFile.getBytes())));
                filesStmt.setString(1, fileHash);
                filesStmt.setString(2, changedFile);
                filesStmt.addBatch();
                changedFilesStmt.setString(1, hash);
                changedFilesStmt.setString(2, fileHash);
                changedFilesStmt.setString(3, repoID);
                changedFilesStmt.addBatch();
            }
            filesStmt.executeBatch();
            changedFilesStmt.executeBatch();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (filesStmt != null) {
                filesStmt.close();
            }
            if (changedFilesStmt != null) {
                changedFilesStmt.close();
            }
        }
    }
