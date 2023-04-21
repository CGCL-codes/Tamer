    private void copiaContexto() throws MojoFailureException, MojoExecutionException {
        getLog().info("Copiando arquivo de Contexto: " + getContextoFile().getAbsolutePath());
        if (!getContextoFile().exists() || getServer() instanceof PlcGlassfishServer) {
            return;
        }
        File destinoContexto = new File(getServer().getContextDir(), this.project.getArtifactId() + ".xml");
        try {
            FileUtils.copyFile(getContextoFile(), destinoContexto);
        } catch (IOException e) {
            throw new MojoFailureException("N�o foi poss�vel copiar o arquivo de contexto para o servidor");
        }
    }
