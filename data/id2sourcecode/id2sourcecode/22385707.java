    public void fileUploaded(ValueChangeEvent event) {
        InputStream in;
        FileOutputStream out;
        String fileUploadLoc = getContentDirectory();
        if (fileUploadLoc == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No hay directorio para guardar los contenidos", null);
            context.addMessage(event.getComponent().getClientId(context), message);
        }
        UploadedFile file = (UploadedFile) event.getNewValue();
        if (file != null && file.getLength() > 0) {
            System.out.println("sizes:" + file.getLength());
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Exito" + " " + file.getFilename() + " (" + file.getLength() + " bytes)");
            context.addMessage(event.getComponent().getClientId(context), message);
            try {
                out = new FileOutputStream(FileManager.concat(fileUploadLoc, file.getFilename()));
                in = file.getInputStream();
                while (in.available() > 0) {
                    out.write(in.read());
                }
                in.close();
                out.close();
                fileName = file.getFilename();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String filename = file != null ? file.getFilename() : null;
            String byteLength = file != null ? "" + file.getLength() : "0";
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "error" + " " + filename + " (" + byteLength + " bytes)", null);
            context.addMessage(event.getComponent().getClientId(context), message);
        }
    }
