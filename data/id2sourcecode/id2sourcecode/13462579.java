    public boolean executeNamedCommand(String nomeComando, Parametro parametros) throws ExcecaoSistema {
        NullPointerException nullEx = null;
        if (nomeComando == null) {
            nullEx = new NullPointerException("O argumento nomeComando possui valor nulo.");
            logger.fatal("Erro ao inicializar os parametros do executeNamedCommand", nullEx);
            throw nullEx;
        }
        logger.debug("executeNamedCommand >> [Executar named command : " + nomeComando + "]");
        EntityManager manager = this.getSessao().getEntityManager();
        Query query = manager.createNamedQuery(nomeComando);
        if (parametros != null) {
            for (Map.Entry<String, Object> e : parametros.entrySet()) {
                logger.debug("Parametros para execu��o - [nomeCampo = " + e.getKey() + "][valorProcurado = " + e.getValue() + "]");
                query.setParameter(e.getKey(), e.getValue());
            }
        } else {
            logger.debug("Os parametros para esta execu��o s�o nulos");
        }
        boolean executed = false;
        try {
            logger.debug("iniciando transa��o...");
            manager.getTransaction().begin();
            logger.debug("executando named command...");
            executed = query.executeUpdate() == 0 ? false : true;
            logger.debug("finalizando execu��o...");
            manager.getTransaction().commit();
            if (executed) logger.debug("comando executado com sucesso!"); else logger.debug("ERRO! comando n�o executado...");
        } catch (Exception ex) {
            logger.error("Erro ao tentar executar um named command " + nomeComando, ex);
            GerenciadorExcecao.tratarExcecao(ex, nomeComando);
        } finally {
            if (manager.getTransaction().isActive()) {
                logger.debug("transa��o ainda est� ativa...");
                logger.debug("fechando transa��o...");
                manager.getTransaction().rollback();
                logger.debug("transa��o fechada com sucesso...");
            }
        }
        logger.debug("executeNamedCommand <<");
        return executed;
    }
