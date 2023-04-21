    public String edit() {
        User user = new User();
        user.setId(new Integer(1));
        boolean reindex = false;
        logger.info("edit() id " + id);
        entry = entryService.getEntryById(id);
        if (!entry.getName().equals(name) || !entry.getAuthor().equals(author)) {
            entry.setName(name);
            entry.setAuthor(author);
            try {
                if (upload != null) {
                    File destFile = new File(getDestFile());
                    FileUtils.copyFile(upload, destFile);
                    edu.csula.coolstatela.model.File fileSpecs = new edu.csula.coolstatela.model.File();
                    fileSpecs.setName(uploadFileName);
                    fileSpecs.setType(uploadContentType);
                    fileSpecs.setOwner(user);
                    fileSpecs.setSize(upload.length());
                    entry.setOwner(user);
                    entry.setFile(fileSpecs);
                }
                entryService.saveEntry(entry);
                reindex = true;
            } catch (IOException ioe) {
                logger.error("EditEntryAction, editEntry ", ioe);
            }
            reindex = true;
        }
        if (reindex) {
            indexer.delete(entry);
            logger.info("Finish deleting index");
            if (uploadContentType != null && uploadContentType.length() > 0) {
                indexer.index(entry, uploadContentType);
            } else {
                indexer.index(entry, entry.getFile().getType());
            }
            logger.info("Finish reindexing");
        }
        return Action.SUCCESS;
    }
