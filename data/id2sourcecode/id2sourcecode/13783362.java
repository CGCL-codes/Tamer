    public boolean crear() {
        int result = 0;
        String sql = "insert into jugadorxdivision" + "(divisionxTorneo_idDivisionxTorneo, jugador_idJugador, posicionFinal, numeroId, ganados, derrotas, " + "empate, estado, puntajeTotal, bye)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement(jxd);
            result = ps.executeUpdate();
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
        return (result > 0);
    }
