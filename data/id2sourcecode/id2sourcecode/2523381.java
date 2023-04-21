    public void copiarContexto(File contextFile, IAppServer server, String finalName) {
        try {
            getLog().info("Adicionando o contexto do Teste: " + contextFile.getAbsolutePath());
            FileUtils.copyFile(contextFile, new File(server.getContextDir(), finalName + ".xml"));
        } catch (Exception e) {
            getLog().error("N�o foi poss�vel copiar o contexto " + contextFile.getAbsolutePath());
        }
    }
