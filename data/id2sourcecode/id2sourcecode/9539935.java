    @Override
    public void update(Empregado empregado) throws SQLException, ClassNotFoundException {
        this.criaConexao(false);
        LOG.debug("Criou a conex�o!");
        String sql = "update empregado set " + "cpf =  ?," + "sexo = ?," + "data_nascimento = ?," + "data_admissao =?," + "data_desligamento =?," + "salario =? ," + "where id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = this.getConnection().prepareStatement(sql);
            LOG.debug("PreparedStatement criado com sucesso!");
            stmt.setString(1, empregado.getCpf());
            stmt.setString(2, empregado.getNome());
            stmt.setString(3, empregado.getSexo());
            stmt.setDate(4, empregado.getDataNascimento());
            stmt.setDate(5, empregado.getDataAdmissao());
            stmt.setDate(6, empregado.getDataDesligamento());
            stmt.setDouble(7, empregado.getSalario());
            stmt.setInt(8, empregado.getId());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                this.getConnection().rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de alterar dados de Revendedor no banco!");
            }
            LOG.debug("Confirmando as altera��es no banco.");
            this.getConnection().commit();
        } catch (SQLException e) {
            LOG.debug("Desfazendo as altera��es no banco.");
        } finally {
            try {
                stmt.close();
                this.fechaConexao();
            } catch (SQLException e) {
            }
        }
    }
