    private boolean doNextImportStep() {
        if (files.size() == 0) {
            return true;
        }
        File file = files.get(fileIndex);
        setJobDescription("Loading File (" + (fileIndex + 1) + "/" + files.size() + ") ... " + file.getName());
        boolean alreadyIncluded = library.contains(file);
        if (alreadyIncluded && !isCopyImages()) {
            allreadyContained++;
        }
        if (!file.isDirectory() && (!alreadyIncluded || isCopyImages())) {
            if (isCopyImages()) {
                try {
                    file = copyFile(file);
                } catch (IOException ioe) {
                    return false;
                }
            }
            Image img = new Image();
            String title = file.getName();
            title = title.substring(0, title.lastIndexOf('.'));
            img.setTitle(title);
            img.setRating(ImageRating.NONE);
            JobFailureDescription desc = new JobFailureDescription();
            desc.setDescription("Error importing file " + file.getName() + ".\n" + "Please make sure the file is accessable.");
            List<JobFailureOption> options = new LinkedList<JobFailureOption>();
            options.add(JobFailureOption.Retry);
            options.add(JobFailureOption.Ignore);
            options.add(JobFailureOption.IgnoreAll);
            options.add(JobFailureOption.Rollback);
            options.add(JobFailureOption.Cancel);
            desc.setOptions(options);
            desc.setRespond(JobFailureOption.Retry);
            boolean loaded = false;
            do {
                try {
                    Image tmp = ImageUtil.resolveImage(file, ImageQuality.getBest(), importId, addedDate);
                    img.setThumbnail(tmp.getThumbnail());
                    img.setMetadata(tmp.getMetadata());
                    img.setExifMetadata(tmp.getExifMetadata());
                    library.add(img);
                    for (Tag t : defaultTags) {
                        img.addTag(t);
                    }
                    if (defaultAlbum != null) {
                        img.addToAlbum(defaultAlbum);
                    }
                    imported++;
                    loaded = true;
                } catch (Exception ioe) {
                    desc.setCause(ioe);
                }
                if (!loaded) {
                    if (ignoreAll) {
                        ignored++;
                        loaded = true;
                    } else {
                        requestFailureHandling(desc);
                        switch(desc.getRespond()) {
                            case Retry:
                                loaded = false;
                                break;
                            case Ignore:
                                ignored++;
                                loaded = true;
                                break;
                            case IgnoreAll:
                                ignored++;
                                loaded = true;
                                ignoreAll = true;
                                break;
                            case Rollback:
                                rollback();
                                return false;
                            case Cancel:
                                cancel();
                                return false;
                        }
                    }
                }
            } while (!loaded);
        }
        fileIndex++;
        return fileIndex >= files.size();
    }
