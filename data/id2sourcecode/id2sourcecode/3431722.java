    public void execute(HttpServletRequest pReq, HttpServletResponse pResp) throws IOException {
        String resourceName = RESOURCES_PKG + pReq.getParameter(PARAM_NAME);
        log.debug("Carregando recurso: " + resourceName);
        setContentType(pReq, pResp);
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        pResp.getOutputStream().write(readBytes(resource));
    }
