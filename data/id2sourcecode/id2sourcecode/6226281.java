        private void sourceButtonActionPerformed(java.awt.event.ActionEvent evt) {
            ConnectionParm con = new ConnectionParm();
            LoadWriteProperties prop = new LoadWriteProperties();
            try {
                prop.loadSourceProperties();
                con.setUsername(prop.conn.getUsername());
                con.setPassword(prop.conn.getPassword());
                con.setHost(prop.conn.getHost());
                con.setPort(prop.conn.getPort());
                con.setSID(prop.conn.getSID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConnectionParmWindow window = new ConnectionParmWindow(con);
            window.setVisible(true);
            ConnectionParm conready = new ConnectionParm();
            if (window.getResult() == 1) {
                conready = window.getParms();
                try {
                    ConnectionParm parm_s = new ConnectionParm(conready.getUsername(), conready.getPassword(), conready.getHost(), conready.getPort(), conready.getSID());
                    Connection conn_s = ConnectionFactory.getConnectionSource(parm_s.getUsername(), parm_s.getPassword(), parm_s.getHost(), parm_s.getPort(), parm_s.getSID());
                    conn_source = new ConnectionBean(conn_s, parm_s);
                    System.out.println("Conexao origem estabelecida com servidor: " + conready.getHost() + " Usuario: " + conready.getUsername().toUpperCase() + " Banco: " + conready.getSID());
                    try {
                        prop.writeSourceProperties(conready);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lbColorSource.setForeground(corG);
                } catch (SQLException e) {
                    lbColorSource.setForeground(corR);
                    System.out.println(e);
                }
            } else {
            }
        }
