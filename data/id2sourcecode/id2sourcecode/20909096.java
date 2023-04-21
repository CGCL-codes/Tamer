    public void salvarFoto(Aluno aluno, Foto foto) {
        if (foto != null) {
            try {
                aluno.setFoto(aluno.getId() + "_" + foto.getNome());
                FacesContext aFacesContext = FacesContext.getCurrentInstance();
                ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
                String path = context.getRealPath(EscolaConfig.getInstance().getUploadPath() + aluno.getFoto());
                File file = new File(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(foto.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = bufferedInputStream.read(buffer)) > 0) fileOutputStream.write(buffer, 0, count);
                } finally {
                    bufferedInputStream.close();
                    fileOutputStream.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
