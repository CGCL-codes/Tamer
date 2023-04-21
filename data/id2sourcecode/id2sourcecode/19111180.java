    public UpdateReturn updateMod(ArrayList<Mod> mods) {
        ExecutorService pool = Executors.newCachedThreadPool();
        Iterator<Mod> it = mods.iterator();
        HashSet<Future<UpdateThread>> temp = new HashSet<Future<UpdateThread>>();
        while (it.hasNext()) {
            Mod tempMod = it.next();
            temp.add(pool.submit(new UpdateThread(tempMod)));
            logger.info("Started update on: " + tempMod.getName() + " - " + tempMod.getVersion());
        }
        HashSet<Future<UpdateThread>> result = new HashSet<Future<UpdateThread>>();
        while (temp.size() != result.size()) {
            Iterator<Future<UpdateThread>> ite = temp.iterator();
            while (ite.hasNext()) {
                Future<UpdateThread> ff = ite.next();
                if (!result.contains(ff) && ff.isDone()) {
                    result.add(ff);
                    int[] ints = new int[2];
                    ints[0] = result.size();
                    ints[1] = temp.size();
                    setChanged();
                    notifyObservers(ints);
                }
            }
        }
        Iterator<Future<UpdateThread>> ite = result.iterator();
        UpdateReturn returnValue = new UpdateReturn();
        while (ite.hasNext()) {
            Future<UpdateThread> ff = ite.next();
            try {
                UpdateThread mod = (UpdateThread) ff.get();
                File file = mod.getFile();
                if (file != null) {
                    new File(mod.getMod().getPath()).setWritable(true);
                    FileUtils.copyFile(file, mod.getMod().getPath());
                    Mod newMod = null;
                    String olderVersion = mod.getMod().getVersion();
                    try {
                        newMod = XML.xmlToMod(new String(ZIP.getFile(file, Mod.MOD_FILENAME)));
                    } catch (StreamException ex) {
                        logger.info("StreamException: Failed to update: " + mod.getMod().getName(), ex);
                        returnValue.addModFailed(mod.getMod(), ex);
                    } catch (ZipException ex) {
                        logger.info("ZipException: Failed to update: " + mod.getMod().getName(), ex);
                        returnValue.addModFailed(mod.getMod(), ex);
                    }
                    if (newMod != null) {
                        newMod.setPath(mod.getMod().getPath());
                        Mod oldMod = getMod(mod.getMod().getName(), olderVersion);
                        boolean wasEnabled = oldMod.isEnabled();
                        HashSet<Mod> gotDisable = new HashSet<Mod>();
                        gotDisable.add(oldMod);
                        while (!gotDisable.isEmpty()) {
                            Iterator<Mod> iter = gotDisable.iterator();
                            while (iter.hasNext()) {
                                try {
                                    Mod next = iter.next();
                                    disableMod(next);
                                    gotDisable.remove(next);
                                } catch (ModEnabledException ex) {
                                    Iterator<Pair<String, String>> itera = ex.getDeps().iterator();
                                    while (itera.hasNext()) {
                                        Pair<String, String> pair = itera.next();
                                        if (!gotDisable.contains(getMod(Tuple.get1(pair), Tuple.get2(pair)))) {
                                            gotDisable.add(getMod(Tuple.get1(pair), Tuple.get2(pair)));
                                        }
                                    }
                                }
                            }
                        }
                        oldMod.copy(newMod);
                        if (wasEnabled) {
                            try {
                                enableMod(newMod, false);
                            } catch (Exception ex) {
                                logger.error("Could not enable mod " + newMod.getName());
                            }
                        }
                        returnValue.addUpdated(mod.getMod(), olderVersion);
                        logger.info(mod.getMod().getName() + " was updated to " + newMod.getVersion() + " from " + olderVersion);
                    }
                } else {
                    logger.info(mod.getMod().getName() + " is up-to-date");
                    returnValue.addUpToDate(mod.getMod());
                }
            } catch (SecurityException ex) {
                logger.info("Couldn't write on the file.");
            } catch (InterruptedException ex) {
            } catch (ExecutionException ex) {
                try {
                    UpdateModException ex2 = (UpdateModException) ex.getCause();
                    logger.info("Failed to update: " + ex2.getMod().getName() + " - " + ex2.getCause().getClass() + " - " + ex2.getCause().getMessage());
                    returnValue.addModFailed(ex2.getMod(), (Exception) ex2.getCause());
                } catch (ClassCastException ex3) {
                    logger.info(ex.getCause());
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                logger.error("Random I/O Exception happened", ex);
            }
        }
        pool.shutdown();
        return returnValue;
    }
