    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURI().replace(request.getContextPath() + request.getServletPath(), "");
        if (requestURL.startsWith("/")) {
            requestURL = requestURL.substring(1);
        }
        requestURL = "arquivos/" + requestURL;
        String arqPath = conf.getArquivosPath() + requestURL;
        File arq = new File(arqPath);
        if (arq.exists()) {
            response.getOutputStream().write(FileUtils.readFileToByteArray(arq));
        } else {
            logger.warn("Arquivo nao encontrado " + arqPath + " requestURL = '" + requestURL + "'");
            response.setStatus(404);
        }
    }
