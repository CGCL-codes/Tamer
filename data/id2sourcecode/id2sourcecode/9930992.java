    public void run() {
        notifyThreadStarted(this);
        try {
            int waitTime = (int) (Math.random() * 5000);
            Mixed.wait(waitTime);
            int index = findFreeDownloadIndex();
            int failures = 0;
            while (failures < maxFailures) {
                if (index == -1) {
                    notifyThreadFinished(this);
                    return;
                }
                File target = File.createTempFile("frost-index-" + index, board.getBoardFilename(), new File(MainFrame.frostSettings.getValue("temp.dir")));
                logger.info("FILEDN: Requesting index " + index + " for board " + board.getBoardName() + " for date " + date);
                FcpResults fcpresults = FcpRequest.getFile(requestKey + index + ".idx.sha3.zip", null, target, requestHtl + ((Integer) indices.elementAt(index)).intValue(), true);
                if (fcpresults != null && target.length() > 0) {
                    setIndexSuccessfull(index);
                    failures = 0;
                    String digest = Core.getCrypto().digest(target);
                    if (Core.getMessageSet().contains(digest)) {
                        target.delete();
                        index = findFreeDownloadIndex();
                        continue;
                    }
                    Core.getMessageSet().add(digest);
                    try {
                        byte[] zippedXml = FileAccess.readByteArray(target);
                        byte[] unzippedXml = FileAccess.readZipFileBinary(target);
                        if (unzippedXml == null) {
                            logger.warning("Could not extract received zip file, skipping.");
                            target.delete();
                            index = findFreeDownloadIndex();
                            continue;
                        }
                        File unzippedTarget = new File(target.getPath() + "_unzipped");
                        FileAccess.writeByteArray(unzippedXml, unzippedTarget);
                        FrostIndex receivedIndex = null;
                        try {
                            receivedIndex = new FrostIndex(XMLTools.parseXmlFile(unzippedTarget, false).getDocumentElement());
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, "Could not parse the index file, skipping.", ex);
                            target.delete();
                            unzippedTarget.delete();
                            index = findFreeDownloadIndex();
                            continue;
                        }
                        Identity sharer = null;
                        Identity sharerInFile = receivedIndex.getSharer();
                        if (fcpresults.getRawMetadata() != null) {
                            SignMetaData md;
                            try {
                                md = new SignMetaData(zippedXml, fcpresults.getRawMetadata());
                            } catch (Throwable t) {
                                logger.log(Level.SEVERE, "Could not read the XML metadata, skipping file index.", t);
                                target.delete();
                                index = findFreeDownloadIndex();
                                continue;
                            }
                            if (sharerInFile == null) {
                                logger.warning("MetaData present, but file didn't contain an identity :(");
                                unzippedTarget.delete();
                                target.delete();
                                index = findFreeDownloadIndex();
                                continue;
                            }
                            String _owner = null;
                            String _pubkey = null;
                            if (md.getPerson() != null) {
                                _owner = Mixed.makeFilename(md.getPerson().getUniqueName());
                                _pubkey = md.getPerson().getKey();
                            }
                            if (_owner == null || _owner.length() == 0 || _pubkey == null || _pubkey.length() == 0) {
                                logger.warning("XML metadata have missing fields, skipping file index.");
                                unzippedTarget.delete();
                                target.delete();
                                index = findFreeDownloadIndex();
                                continue;
                            }
                            if (!_owner.equals(Mixed.makeFilename(sharerInFile.getUniqueName())) || !_pubkey.equals(sharerInFile.getKey())) {
                                logger.warning("The identity in MetaData didn't match the identity in File! :(\n" + "file owner : " + sharerInFile.getUniqueName() + "\n" + "file key : " + sharerInFile.getKey() + "\n" + "meta owner: " + _owner + "\n" + "meta key : " + _pubkey);
                                unzippedTarget.delete();
                                target.delete();
                                index = findFreeDownloadIndex();
                                continue;
                            }
                            boolean valid = Core.getCrypto().detachedVerify(zippedXml, _pubkey, md.getSig());
                            if (valid == false) {
                                logger.warning("Invalid sign for index file from " + _owner);
                                unzippedTarget.delete();
                                target.delete();
                                index = findFreeDownloadIndex();
                                continue;
                            }
                            if (identities.getMyId().getUniqueName().trim().equals(_owner)) {
                                logger.info("Received index file from myself");
                                sharer = identities.getMyId();
                            } else {
                                String message = "Received index file from " + _owner;
                                if (identities.getFriends().containsKey(_owner)) {
                                    sharer = identities.getFriends().get(_owner);
                                    logger.info(message + ", a friend");
                                } else if (identities.getNeutrals().containsKey(_owner)) {
                                    sharer = identities.getNeutrals().get(_owner);
                                    logger.info(message + ", a neutral");
                                } else if (identities.getEnemies().containsKey(_owner)) {
                                    if (MainFrame.frostSettings.getBoolValue("hideBadFiles")) {
                                        logger.info("Skipped index file from BAD user " + _owner);
                                        target.delete();
                                        unzippedTarget.delete();
                                        index = findFreeDownloadIndex();
                                        continue;
                                    }
                                    sharer = identities.getEnemies().get(_owner);
                                    logger.info(message + ", an enemy");
                                } else {
                                    logger.info(message + ", a new contact");
                                    sharer = addNewSharer(_owner, _pubkey);
                                    if (sharer == null) {
                                        logger.info("sharer was null... :(");
                                        unzippedTarget.delete();
                                        target.delete();
                                        index = findFreeDownloadIndex();
                                        continue;
                                    }
                                }
                            }
                        } else if (MainFrame.frostSettings.getBoolValue("hideAnonFiles")) {
                            unzippedTarget.delete();
                            target.delete();
                            index = findFreeDownloadIndex();
                            continue;
                        }
                        if (sharer == null || identities.getFriends().containsKey(sharer.getUniqueName()) == false) {
                            String _sharer = sharer == null ? "Anonymous" : sharer.getUniqueName();
                            logger.info("adding only files from " + _sharer);
                            Index.add(receivedIndex, board, _sharer);
                        } else {
                            logger.info("adding all files from " + sharer.getUniqueName());
                            Index.add(unzippedTarget, board, sharer);
                        }
                        target.delete();
                        unzippedTarget.delete();
                    } catch (Throwable t) {
                        logger.log(Level.SEVERE, "Error in UpdateIdThread", t);
                    }
                    index = findFreeDownloadIndex(index);
                    failures = 0;
                } else {
                    target.delete();
                    setIndexFailed(index);
                    failures++;
                    index = findFreeDownloadIndex(index);
                }
            }
            if (isInterrupted()) {
                notifyThreadFinished(this);
                return;
            }
            FrostIndex frostIndex = makeIndexFile();
            if (frostIndex != null) {
                logger.info("FILEDN: Starting upload of index file to board '" + board.toString());
                uploadIndexFile(frostIndex);
            } else {
                logger.info("FILEDN: No keys to upload, stopping UpdateIdThread for " + board.toString());
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Oo. EXCEPTION in UpdateIdThread", t);
        }
        notifyThreadFinished(this);
        resetIndices();
        commit();
    }
