    public void process(MediaItem mi) {
        if (!keepSearching) {
            throw new StopProcessException();
        }
        if (mi instanceof Picture) {
            Picture pic = (Picture) mi;
            isi.setTaskStatus("Processing " + pic.getName());
            log.debug("Processing " + pic.getName());
            PictureDescriptionInfo pdi = pic.getPictureDescriptionInfo();
            pdi.readExifData(overwrite);
            isi.anotherTaskCompleted();
            pdi.saveFile();
        }
    }
