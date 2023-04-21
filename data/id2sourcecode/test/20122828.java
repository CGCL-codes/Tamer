    @Logic(parameters = "id")
    public void curriculum(long id) throws IOException {
        Curriculum curriculum = daoFactory.getCurriculumDao().load(id);
        String fileName = curriculum.getInternalName();
        File file = new File(context.getRealPath(Curriculum.UPLOADED_FILES_PATH + "/" + fileName));
        if (!file.exists()) {
            System.out.println("Erro!!!");
        }
        response.setContentType(curriculum.getRealFileContentType());
        response.setHeader("Content-disposition", "attachment; filename=" + curriculum.getRealFileName());
        response.setContentLength((int) file.length());
        OutputStream stream = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[16384];
        while (fis.available() != 0) {
            int read = fis.read(buffer);
            stream.write(buffer, 0, read);
        }
        stream.flush();
        response.flushBuffer();
    }
