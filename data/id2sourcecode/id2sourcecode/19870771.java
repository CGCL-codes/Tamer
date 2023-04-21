    @RequestMapping(value = "/fileview.html", method = RequestMethod.GET)
    public void openFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") int idAnexo) throws IOException {
        Anexo anexo = anexoService.selectById(idAnexo);
        File file = new File(config.baseDir + "/arquivos_upload_direto/" + anexo.getAnexoCaminho());
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + anexo.getAnexoNome().replace("/[^a-zA-Z0-9_\\(\\*\\)\\.\\s:;,%$!@#&ãàáâäèéêëìíîïõòóôöùúûüçÃÀÁÂÄÈÉÊËÌÍÎÏÖÒÓÔÙÚÛÜÇ]+/g", "-") + "\"");
        ServletOutputStream ouputStream = response.getOutputStream();
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            ouputStream.write(buffer, 0, read);
        }
        ouputStream.flush();
        ouputStream.close();
    }
