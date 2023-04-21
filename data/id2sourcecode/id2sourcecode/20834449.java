    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; cell-densities.zip");
        OutputStream out = response.getOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(new ZipEntry("taxon-cell-density.kml"));
        VelocityContext velocityContext = new VelocityContext(map);
        velocityContext.put("hostUrl", request.getHeader("host"));
        Object results = map.get(resultsModelKey);
        writeTemplate(velocityContext, request, headerTemplatePath, zout);
        writeTemplate(velocityContext, request, templatePath, zout);
        writeTemplate(velocityContext, request, footerTemplatePath, zout);
        zout.closeEntry();
        zout.close();
    }
