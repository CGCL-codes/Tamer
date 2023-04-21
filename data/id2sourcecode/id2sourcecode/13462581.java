    public boolean callStoredProcedure(String nomeProcedure, Parametro parametros) throws ExcecaoSistema {
        NullPointerException nullEx = null;
        if (nomeProcedure == null) {
            nullEx = new NullPointerException("O argumento nomeProcedure possui valor nulo.");
            logger.fatal("Erro ao inicializar os parametros do callStoredProcedure", nullEx);
            throw nullEx;
        }
        logger.debug("callStoredProcedure >> [Executar stored procedure: " + nomeProcedure + "]");
        String procedureCall = "{Call " + nomeProcedure.toUpperCase() + "(";
        int i = 0;
        if (parametros != null && (i = parametros.keySet().size()) > 0) {
            procedureCall += "?";
            for (int n = 1; n < i; n++) {
                procedureCall += ", ?";
            }
        }
        procedureCall += ")}";
        logger.debug("callStoredProcedure >> [Executar stored procedure: " + procedureCall + "]");
        EntityManager manager = this.getSessao().getEntityManager();
        Query query = manager.createNativeQuery(procedureCall);
        int nrParametro = 1;
        if (parametros != null) {
            for (Map.Entry<String, Object> e : parametros.entrySet()) {
                logger.debug("Parametros para execu��o - [ordemCampo = " + nrParametro + "]" + "[nomeCampo = " + e.getKey() + "]" + "[valorEnviado = " + e.getValue() + "]");
                query.setParameter(nrParametro++, e.getValue());
            }
        } else {
            logger.debug("Os parametros para esta execu��o s�o nulos");
        }
        boolean executed = false;
        try {
            logger.debug("iniciando transa��o...");
            manager.getTransaction().begin();
            logger.debug("executando stored procedure...");
            executed = query.executeUpdate() == 0 ? false : true;
            logger.debug("finalizando execu��o...");
            manager.getTransaction().commit();
            if (executed) logger.debug("stored procedure executada com sucesso!"); else logger.debug("ERRO! stored procedure n�o executada...");
        } catch (Exception ex) {
            logger.error("Erro ao tentar executar uma stored procedure " + nomeProcedure, ex);
            GerenciadorExcecao.tratarExcecao(ex, nomeProcedure);
        } finally {
            if (manager.getTransaction().isActive()) {
                logger.debug("transa��o ainda est� ativa...");
                logger.debug("fechando transa��o...");
                manager.getTransaction().rollback();
                logger.debug("transa��o fechada com sucesso...");
            }
        }
        logger.debug("callStoredProcedure <<");
        return executed;
    }
