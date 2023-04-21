    public void retificarPaginasTrabalho(SitioPro sitio) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Date dataAtual = new Date();
        String pastaBkp = Arquivista.pastaDeArquivosDoSitio + "/bkp/" + sdf.format(dataAtual);
        File dirBkp = new File(pastaBkp);
        if (!dirBkp.exists()) {
            dirBkp.mkdirs();
        }
        File dirHtml = new File(Arquivista.pastaDeArquivosDoSitio);
        File arquivosHtml[] = dirHtml.listFiles(new FileFilter() {

            @Override
            public boolean accept(File arq) {
                if (arq.getName().endsWith(".html")) return true;
                return false;
            }
        });
        int c = 1;
        logger.debug("Retificando " + arquivosHtml.length + " arquivos");
        DesignerCss designerCss = new DesignerCss();
        for (File file : arquivosHtml) {
            String nomeArq = G_File.getName(file.getAbsolutePath());
            FileUtils.copyFile(file, new File(pastaBkp + "/" + nomeArq));
            G_File arqHtml = new G_File(file.getAbsolutePath());
            String codHtml = retificador.consertarErroAcessibilidade(arqHtml.read());
            codHtml = retificador.consertarErroHtml(codHtml);
            codHtml = designerCss.retirarCssInline(codHtml, nomeArq);
            arqHtml.write(codHtml);
            logger.debug("Retificado " + nomeArq + " " + (c++) + " de " + arquivosHtml.length);
        }
    }
