    public void dump() {
        if (jtextArea.getText() != " ") {
            try {
                File f2 = new File("dump.txt");
                FileWriter fw = new FileWriter(f2);
                if (rs != null) rs.close();
                rs = stmt.executeQuery(jtextArea.getText());
                ResultSetMetaData rsmd = rs.getMetaData();
                fw.write("---------------------------------\n");
                while (rs.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String columnName = rsmd.getColumnLabel(i);
                        InputStream bis = rs.getAsciiStream(columnName);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        int ch = 0;
                        while ((ch = bis.read()) != -1) bos.write(ch);
                        System.out.println("DUMP DATA:" + new String(bos.toByteArray()) + "\n");
                        fw.write(columnName + " = " + new String(bos.toByteArray()));
                        fw.write("\n");
                    }
                    fw.write("---------------------------------\n");
                }
                fw.close();
            } catch (Throwable e) {
                warnme("" + e.getMessage());
            }
            warnme("Done dump to file dump.txt");
        }
    }
