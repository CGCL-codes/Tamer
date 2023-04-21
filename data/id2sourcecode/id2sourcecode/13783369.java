    public boolean eliminarJugador(int idJxd) {
        int intResult = 0;
        String sql = "UPDATE jugadorxdivision " + " SET estado = 'X' " + " WHERE idJugadorxDivision = " + idJxd;
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
