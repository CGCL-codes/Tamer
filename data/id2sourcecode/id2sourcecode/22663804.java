    @Override
    public void onExecute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        File file = new File(BusinessUnit.getInstance().getBusinessModel().getAbsoluteFilename("mobile/mobile_model.zip"));
        FileInputStream inputStream = null;
        OutputStream outputStream = httpResponse.getOutputStream();
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        try {
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) > -1) outputStream.write(buffer, 0, bytesRead);
        } finally {
            StreamHelper.close(inputStream);
            StreamHelper.close(outputStream);
        }
    }
