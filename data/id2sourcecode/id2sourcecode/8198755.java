    private static void insertData(String table, String columns, String path) {
        initSql();
        String nextLine[];
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ';', '\"');
            while ((nextLine = reader.readNext()) != null) {
                String insert = "INSERT INTO " + table + "(" + columns + ") VALUES (";
                for (String s : nextLine) insert += "\"" + s + "\"" + ", ";
                insert = insert.substring(0, insert.length() - 2);
                insert += ") ";
                try {
                    Statement stat = provider.getConn().createStatement();
                    stat.executeUpdate(insert);
                    stat.close();
                } catch (SQLException e) {
                    System.out.print(java.util.Arrays.toString(nextLine));
                    e.printStackTrace();
                }
            }
            reader.close();
            try {
                provider.getConn().commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
