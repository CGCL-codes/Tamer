    public void copiarWar(IAppServer server, File targetDir, String finalName) throws MojoExecutionException {
        getLog().info("Copiando o war file para " + server.getAppDir());
        try {
            FileUtils.copyFile(new File(targetDir, finalName + ".war"), new File(server.getWebappsDir(), finalName + ".war"));
        } catch (Exception e) {
            throw new MojoExecutionException("N�o foi poss�vel copiar o arquivo " + new File(targetDir, finalName + ".war") + ".war para o Web Server");
        }
    }
