    @Override
    public String execute() throws Exception {
        Usuario usuarioLogado = getUsuarioLogado();
        if (fileUpload != null && !fileUploadFileName.equals("")) {
            try {
                File zipFile = null;
                {
                    String nomeRelativo = PASTA_TEMPLATE + '/' + fileUploadFileName;
                    String fullFileName = getServletContext().getRealPath(nomeRelativo);
                    zipFile = new File(fullFileName);
                    FileUtils.copyFile(fileUpload, zipFile);
                }
                File folderDestino = new File(getServletContext().getRealPath(PASTA_TEMPLATE + '/' + fileUploadFileName.substring(0, fileUploadFileName.lastIndexOf("."))));
                if (folderDestino.exists()) {
                    FileUtils.cleanDirectory(folderDestino);
                    FileUtils.deleteDirectory(folderDestino);
                }
                folderDestino = new File(getServletContext().getRealPath(PASTA_TEMPLATE));
                descompactar(zipFile, folderDestino);
                usuarioLogado.addActionMessage("Template instalado com sucesso.");
                usuarioLogado.addActionMessage("Pasta de destino:" + folderDestino.getAbsolutePath());
                LogDao.getInstance().addLog(usuarioLogado, "Instalou o template " + fileUploadFileName);
            } catch (Exception e) {
                usuarioLogado.addActionError("Erro ao instalar o template. " + e.getMessage());
                logger.error("Erro ao instalar template", e);
            }
        }
        return SUCCESS;
    }
