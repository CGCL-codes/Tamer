    @Override
    public void downloadFile(String contentId, HttpServletResponse response) {
        try {
            FileContent fileContent = fileContentService.findById(contentId).getOrElse(null);
            File file = fileService.getProfileWorkFile(fileContent.getData());
            response.setContentType(new MimetypesFileTypeMap().getContentType(file));
            try {
                FileInputStream fileStream = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int ch;
                while ((ch = fileStream.read()) != -1) baos.write(ch);
                fileStream.close();
                response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));
                response.getOutputStream().write(baos.toByteArray());
                baos.close();
            } catch (Exception ex) {
                LOG.debug("An error occurs during reading file , overcome this.", ex);
            }
        } catch (BusinessServiceException ex) {
            ex.printStackTrace();
            if (LOG.isDebugEnabled()) {
                LOG.debug("An error occurs during download file , overcome this.", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("An error occurs during download file with message [{}], overcome this.", ex.getMessage());
            }
        }
    }
