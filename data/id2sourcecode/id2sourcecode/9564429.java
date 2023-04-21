    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURL().toString();
        ArquivoAction arquivoAction = new ArquivoAction();
        String barra = String.valueOf(File.separatorChar);
        try {
            String pathLocal = arquivoAction.getArquivosPath();
            if (!pathLocal.endsWith(barra)) {
                pathLocal = pathLocal + barra;
            }
            String[] urlSplitted = url.split("/imagens/");
            Sitio sitio = (Sitio) httpRequest.getSession().getAttribute(Gerenciador.KEY_ULTIMO_SITIO);
            if (sitio == null) {
                sitio = Gerenciador.encontrarSitioUrl(url);
            }
            String pastaSitio = sitio.getNoPastaArquivos();
            StringBuilder pathArquivoLocal = new StringBuilder(pathLocal);
            pathArquivoLocal.append(pastaSitio);
            pathArquivoLocal.append(barra);
            pathArquivoLocal.append("imagens");
            pathArquivoLocal.append(barra);
            pathArquivoLocal.append(urlSplitted[1]);
            File arquivo = new File(pathArquivoLocal.toString());
            if (arquivo.exists()) {
                response.getOutputStream().write(FileUtils.readFileToByteArray(arquivo));
            } else {
                chain.doFilter(request, response);
                logger.warn("404 pathArquivoLocal = " + pathArquivoLocal);
            }
        } catch (Exception e) {
            logger.error("Erro ao filtrar arquivo", e);
        }
    }
