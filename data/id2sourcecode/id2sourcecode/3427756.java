    private void copiaArquivoContexto() throws IOException {
        if (!getContextoFile().exists()) {
            getLog().info("O arquivo de contexto n�o existe: " + getContextoFile().getAbsolutePath());
            return;
        }
        getLog().info("Copiando arquivo de contexto " + getContextoFile().getName());
        File destinoContexto = new File(getAppInTargetDir(), "/META-INF/context.xml");
        if (destinoContexto.lastModified() < getContextoFile().lastModified() || destinoContexto.length() != getContextoFile().length()) {
            File contexto = null;
            if (isAmbiente(AMBIENTE_CI)) {
                contexto = getContextoFile(PlcAbstractMojo.CONTEXTO_PROD);
            } else contexto = getContextoFile();
            FileUtils.copyFile(contexto, destinoContexto);
        }
    }
