    public Annotation get(final File imageFile) {
        synchronized (writeLock) {
            if (unreadables.contains(imageFile.getAbsolutePath()) || unwriteables.contains(imageFile.getAbsolutePath())) {
                return new BlacklistedAnnotation(imageFile);
            }
            if (alreadyA(imageFile)) {
                return new PlaceholderAnnotation(imageFile, getA(imageFile));
            }
            String uniqueImageId = null;
            try {
                uniqueImageId = getImageUniqueId(imageFile);
            } catch (Throwable t) {
                LOG.error("Blacklisting '" + imageFile + "' for unreadable exif data", t);
                unreadables.add(imageFile.getAbsolutePath());
                return new BlacklistedAnnotation(imageFile);
            }
            if (annotationExists(uniqueImageId)) {
                return new PlaceholderAnnotation(imageFile, uniqueImageId);
            }
            final ExifMetadata metadata = metaDataReader.read(imageFile);
            if (StringUtils.isEmpty(uniqueImageId)) {
                final String newUniqueImageId = createUniqueImageId();
                addA(imageFile, newUniqueImageId);
                taskRunner.add(new Task() {

                    public String getName() {
                        return imageFile.getName();
                    }

                    public void execute() {
                        try {
                            exifMetadataWriter.setImageUniqueId(imageFile, newUniqueImageId);
                            LOG.info("Image unique id " + newUniqueImageId + " injected into " + imageFile);
                            removeA(imageFile);
                            eventBus.publish(new AnnotationUpdatedBusEvent(read(newUniqueImageId)));
                        } catch (Throwable t) {
                            LOG.error("Blacklisting '" + imageFile + "' for unwriteable exif data", t);
                            unwriteables.add(imageFile.getAbsolutePath());
                            eventBus.publish(new FolderUpdatedBusEvent(new Folder(imageFile.getParentFile())));
                        }
                    }
                }, true);
                return new PlaceholderAnnotation(imageFile, newUniqueImageId);
            } else {
                final String finalUniqueImageId = uniqueImageId;
                addA(imageFile, uniqueImageId);
                taskRunner.add(new Task() {

                    public String getName() {
                        return imageFile.getName();
                    }

                    public void execute() {
                        String userComment = metadata.get("UserComment");
                        if (StringUtils.isEmpty(userComment)) {
                            userComment = Utils.stripExtension(imageFile.getName());
                        }
                        Annotation newAnnotation = new BaseAnnotation(finalUniqueImageId, Utils.stripExtension(imageFile.getName()), new Date(), imageFile.getParent(), imageFile.getName(), null, null, userComment, null, metadata);
                        store(newAnnotation);
                        removeA(imageFile);
                        eventBus.publish(new FolderUpdatedBusEvent(new Folder(imageFile.getParentFile())));
                    }
                }, true);
                return new PlaceholderAnnotation(imageFile, finalUniqueImageId);
            }
        }
    }
