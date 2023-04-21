        private void targetButtonActionPerformed(java.awt.event.ActionEvent evt) {
            ConnectionParm con = new ConnectionParm();
            LoadWriteProperties prop = new LoadWriteProperties();
            try {
                prop.loadTargetProperties();
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
            if (window.getResult() == 1) {
                try {
                    ConnectionParm conready = new ConnectionParm();
                    conready = window.getParms();
                    ConnectionParm parm_t = new ConnectionParm(conready.getUsername(), conready.getPassword(), conready.getHost(), conready.getPort(), conready.getSID());
                    Connection conn_t = ConnectionFactory.getConnectionTarget(parm_t.getUsername(), parm_t.getPassword(), parm_t.getHost(), parm_t.getPort(), parm_t.getSID());
                    conn_target = new ConnectionBean(conn_t, parm_t);
                    prop.writeTargetProperties(conready);
                    System.out.println("Conexao destino estabelecida com servidor: " + conready.getHost() + " Usuario: " + conready.getUsername().toUpperCase() + " Banco: " + conready.getSID());
                    lbColorTarget.setForeground(corG);
                } catch (Exception e) {
                    lbColorTarget.setForeground(corR);
                    System.out.println(e);
                }
            } else {
            }
        }
