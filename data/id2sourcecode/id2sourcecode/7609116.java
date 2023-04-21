    public void setUploadedFile(UploadedFile uploadedFile) {
        if (null != uploadedFile) {
            ProductBean bean = getBean();
            ResourceFileWithData rf = bean.getDrawingFile();
            if (null == rf) {
                rf = new ResourceFileWithData();
                bean.setDrawingFile(rf);
            }
            Date now = new Date();
            if (null == rf.getCtime()) {
                rf.setCtime(now);
            }
            if (null == rf.getOwner()) {
                rf.setOwner(FacesUtil.getCurrentContextUser(UserAccount.class, false));
            }
            rf.setMtime(now);
            rf.setName(UploadFile.cleanupName(uploadedFile.getName()));
            rf.setContentType(uploadedFile.getContentType());
            rf.setSize(uploadedFile.getSize());
            try {
                BufferedInputStream in = new BufferedInputStream(uploadedFile.getInputStream());
                File f = File.createTempFile("QNRF", ".tmp");
                getTempFiles().add(f);
                f.deleteOnExit();
                rf.setTempFile(f);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
                final byte[] READ_BUF = new byte[1024];
                int read;
                do {
                    read = in.read(READ_BUF);
                    if (read > 0) {
                        out.write(READ_BUF, 0, read);
                    }
                } while (read > 0);
                out.flush();
                out.close();
                in.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String name = UploadFile.cleanupName(uploadedFile.getName());
            rf.setName(name);
        }
    }
