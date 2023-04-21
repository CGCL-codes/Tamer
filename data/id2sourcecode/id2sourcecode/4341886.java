    @Override
    public void finalTestConditions() {
        while (!MapSites.getInstance().isEmpty()) try {
            Thread.currentThread();
            Thread.sleep(1000);
            SimpleLog.getInstance().writeLog(3, "Aguardando finalização de alguma Thread");
        } catch (InterruptedException e) {
            SimpleLog.getInstance().writeException(e, 3);
        }
        System.out.println("Consolidação das quantidades de links finalizada.");
        System.out.println("Total null: " + countNull / 2);
        System.out.println("Total não null: " + countNotNull / 2);
        System.out.println("Quantidade total de domínios: " + MapSites.getInstance().getMapDomCounter());
        System.out.println("Quantidade total de extensoes: " + MapSites.getInstance().getMapExtCounter());
        System.out.println("Quantidade total de links: " + MapSites.getInstance().getTotalLinks());
        System.out.println("Quantidade total de linhas lidas: " + this.linhasLidas);
        System.out.println("Quantidade total de linhas lidas validas: " + this.linhasValidas);
        System.out.println("Quantidade total de arquivos lidos: " + this.arquivosLidos);
        System.out.println("Terminou às " + new Date(System.currentTimeMillis()).toString());
        SimpleLog.getInstance().writeLog(5, "==================================");
        SimpleLog.getInstance().writeLog(5, "Páginas com resultado null: ");
        for (Iterator<Long> it = nullPageIds.iterator(); it.hasNext(); ) {
            SimpleLog.getInstance().writeLog(5, it.next().toString());
        }
        SimpleLog.getInstance().writeLog(5, "==================================");
        System.out.println("Links Invalidos: " + MapSites.getInstance().getLinksInvalidos());
        System.out.println("Links Escritos no Banco de Dados de Links: " + MapSites.getInstance().getDBLinksCounter());
        System.out.println("Links Escritos no Banco de Dados de Dominios:" + MapSites.getInstance().getDBDomCounter());
        System.out.println("Links Escritos no Banco de Dados de Extensoes: " + MapSites.getInstance().getDBExtCounter());
    }
