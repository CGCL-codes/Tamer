    public void fieldToFile(ResultSet rs, String columnName, String destFilePathName) throws SQLException, SmartUploadException, IOException, ServletException {
        try {
            if (m_application.getRealPath(destFilePathName) != null) destFilePathName = m_application.getRealPath(destFilePathName);
            InputStream is_data = rs.getBinaryStream(columnName);
            FileOutputStream file = new FileOutputStream(destFilePathName);
            int c;
            while ((c = is_data.read()) != -1) file.write(c);
            file.close();
        } catch (Exception e) {
            throw new SmartUploadException("Unable to save file from the DataBase (1020).");
        }
    }
