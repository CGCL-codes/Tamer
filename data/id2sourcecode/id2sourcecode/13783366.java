    public boolean asignarIdDivisionTorneo(jugadorxDivision jxd, int idDiv, int valor) {
        int intResult = 0;
        String sql = "UPDATE jugadorxdivision " + " SET numeroId = " + valor + " " + " WHERE jugador_idJugador = " + jxd.getIdJugador() + " AND divisionxTorneo_idDivisionxTorneo = " + idDiv;
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            intResult = ps.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exe) {
                exe.printStackTrace();
            }
        } finally {
            conexionBD.close(ps);
            conexionBD.close(connection);
        }
        return (intResult > 0);
    }
