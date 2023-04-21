    public String save() throws Exception {
        User user = new User();
        user.setId(new Integer(1));
        Entry entry = new Entry(name, author, user);
        File destFile = new File(getDestFile());
        try {
            FileUtils.copyFile(upload, destFile);
            edu.csula.coolstatela.model.File fileSpecs = new edu.csula.coolstatela.model.File();
            fileSpecs.setName(uploadFileName);
            fileSpecs.setType(uploadContentType);
            fileSpecs.setOwner(user);
            fileSpecs.setSize(upload.length());
            entry.setOwner(user);
            entry.setFile(fileSpecs);
            entryService.saveEntry(entry);
            indexer.index(entry, uploadContentType);
            logger.info("Success!");
        } catch (IOException ioe) {
            logger.error("IOException copyFile() ", ioe);
            return INPUT;
        } catch (Exception e) {
            logger.error("Exception e ", e);
            return INPUT;
        }
        return SUCCESS;
    }
