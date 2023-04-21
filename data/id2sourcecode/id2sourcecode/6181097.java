        public InstallJarResult(URL url, String name, String[] className) throws Exception {
            this.className = className;
            PreparedStatement jarStmt = conn.prepareStatement("execute block (name type of column fb$java$jar.name = ?)\n" + "	returns (id type of column fb$java$jar.id)\n" + "as\n" + "begin\n" + "	insert into fb$java$jar (id, name, owner)\n" + "		values (next value for fb$java$seq, :name, current_user)\n" + "		returning id into id;\n" + "	suspend;\n" + "end");
            try {
                jarStmt.setString(1, name);
                ResultSet rs = jarStmt.executeQuery();
                rs.next();
                long jarId = rs.getLong(1);
                rs.close();
                entryStmt = conn.prepareStatement("insert into fb$java$jar_entry (id, jar, name, content)\n" + "	values (next value for fb$java$seq, ?, ?, ?)");
                entryStmt.setLong(1, jarId);
                jarStream = new JarInputStream(url.openStream());
                buffer = new byte[8192];
            } finally {
                jarStmt.close();
            }
        }
