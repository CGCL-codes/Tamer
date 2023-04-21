    public static String getLargeTextField(ResultSet rs, String columnname) throws SQLException {
        Reader bodyReader;
        String value;
        try {
            bodyReader = rs.getCharacterStream(columnname);
            if (bodyReader == null) return "";
            char buf[] = new char[256];
            StringWriter out = new StringWriter(255);
            int i;
            while ((i = bodyReader.read(buf)) >= 0) out.write(buf, 0, i);
            value = out.toString();
            out.close();
            bodyReader.close();
        } catch (Exception e) {
            return rs.getString(columnname);
        }
        return value;
    }
