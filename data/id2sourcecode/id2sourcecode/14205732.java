    protected boolean doDeploy(DeployTask deployTask) {
        Deploy deploy = deployTask.getDeploy();
        boolean isPackaged;
        String[] splits = deploy.getDeployType().split("/");
        String audioType = splits[0];
        String packageType = splits[1];
        if ("packaged".equals(packageType)) {
            isPackaged = true;
        } else {
            isPackaged = false;
        }
        int subType = ResourceTypesUtil.getAudioSubType(audioType);
        String ext = ResourceTypesUtil.audioSubTypeToExt(subType);
        String bookURL;
        String audioURL;
        boolean isSWF = subType == ResourceTypes.SUBTYPE_AUDIO_SWF;
        String bookUuid = deploy.getAudio().getRecordTask().getChapter().getUuid();
        String audioUuid;
        if (subType == ResourceTypes.SUBTYPE_AUDIO_MP3) {
            audioUuid = deploy.getAudio().getUuid();
        } else {
            audioUuid = deploy.getUuid();
        }
        if (isPackaged) {
            bookURL = "content/" + bookUuid + ".txt";
            audioURL = "content/" + audioUuid + "." + ext;
        } else {
            bookURL = getHttpURL(ResourceTypes.TYPE_BOOK, ResourceTypes.SUBTYPE_RESERVED, bookUuid);
            audioURL = getHttpURL(ResourceTypes.TYPE_AUDIO, subType, audioUuid);
        }
        try {
            fireDeployStartEvent(deployTask);
            BufferedReader syncFileReader = null;
            BufferedWriter deploySyncWriter = null;
            try {
                String syncUuid = deploy.getAudio().getSync().getUuid();
                syncFileReader = new BufferedReader(new FileReader(storageService.getObjectLocation(ResourceTypes.TYPE_SYNC, ResourceTypes.SUBTYPE_RESERVED, syncUuid)));
                String deployUuid = deploy.getUuid();
                deploySyncWriter = new BufferedWriter(new FileWriter(storageService.getObjectLocation(ResourceTypes.TYPE_DEPLOY, ResourceTypes.SUBTYPE_DEPLOY_SYNC, deployUuid)));
                String line = null;
                while ((line = syncFileReader.readLine()) != null) {
                    StringBuffer lineToWrite = new StringBuffer();
                    Matcher placeHolderMatcher = syncPlaceHolder.matcher(line);
                    while (placeHolderMatcher.find()) {
                        String toReplace = null;
                        String coreStr = placeHolderMatcher.group(1);
                        if (coreStr.equals("format")) {
                            toReplace = isSWF ? "swf" : "audio";
                        } else {
                            if (coreStr.startsWith("book:")) {
                                toReplace = bookURL;
                            } else if (coreStr.startsWith("audio:")) {
                                toReplace = audioURL;
                            }
                        }
                        if (toReplace != null) {
                            placeHolderMatcher.appendReplacement(lineToWrite, toReplace);
                        }
                    }
                    placeHolderMatcher.appendTail(lineToWrite);
                    deploySyncWriter.write(lineToWrite.toString() + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (syncFileReader != null) {
                        syncFileReader.close();
                    }
                    if (deploySyncWriter != null) {
                        deploySyncWriter.close();
                    }
                } catch (IOException e) {
                }
            }
            if (isPackaged) {
                ZipOutputStream deployFile = null;
                try {
                    String deployUuid = deploy.getUuid();
                    deployFile = new ZipOutputStream(new FileOutputStream(storageService.getObjectLocation(ResourceTypes.TYPE_DEPLOY, ResourceTypes.SUBTYPE_DEPLOY_ZIP, deployUuid)));
                    ZipEntry entry = new ZipEntry(bookURL);
                    deployFile.putNextEntry(entry);
                    storageService.loadStream(ResourceTypes.TYPE_BOOK, ResourceTypes.SUBTYPE_RESERVED, bookUuid, deployFile);
                    deployFile.closeEntry();
                    entry = new ZipEntry(audioURL);
                    deployFile.putNextEntry(entry);
                    storageService.loadStream(ResourceTypes.TYPE_DEPLOY, subType, audioUuid, deployFile);
                    deployFile.closeEntry();
                    entry = new ZipEntry("content/syncFile.xml");
                    deployFile.putNextEntry(entry);
                    storageService.loadStream(ResourceTypes.TYPE_DEPLOY, ResourceTypes.SUBTYPE_DEPLOY_SYNC, deploy.getUuid(), deployFile);
                    deployFile.closeEntry();
                    String appRoot = null;
                    if (deployAppRoot != null) {
                        appRoot = deployAppRoot;
                    } else {
                        File ctxRoot = appCtx.getResource("/").getFile();
                        appRoot = ctxRoot.getCanonicalPath() + File.separator + "DeployApp";
                    }
                    zipFolder(deployFile, appRoot, "");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (UnsupportedStorageObject e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (deployFile != null) {
                            deployFile.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            return true;
        } finally {
            fireDeployDoneEvent(deployTask);
        }
    }
