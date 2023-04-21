    @SuppressWarnings({ "unchecked", "unused" })
    protected int[] makeStatFactors() {
        File cacheFile = FileUtil.resolve(RGIS.getOutputFolder(), String.format("phase1-cache-%d.bin", initialSeed));
        final String P1P_RESULT = "numRealizations2Make";
        final String P1P_LAST_MODIFIED = "last-modified";
        final String P1P_RANDOM_SEED = "random-seed";
        Date inputTime = householdArchTypesTime.after(configTime) ? householdArchTypesTime : configTime;
        int[] numRealizations2Make = null;
        synchronized (cacheFileLock) {
            while (numRealizations2Make == null) {
                if (cacheFile.exists()) {
                    HashMap<String, Object> props = null;
                    ObjectInputStream in = null;
                    FileLock lock = null;
                    try {
                        FileInputStream fin = new FileInputStream(cacheFile);
                        lock = fin.getChannel().lock(0, Long.MAX_VALUE, true);
                        in = new ObjectInputStream(fin);
                        props = (HashMap<String, Object>) in.readObject();
                        in.close();
                    } catch (IOException e) {
                        log.warning("Unable to read phase-1 cache file: " + ObjectUtil.getMessage(e));
                    } catch (ClassNotFoundException e) {
                        LogUtil.cr(log);
                        log.warning("Unable to read phase-1 cache file: " + ObjectUtil.getMessage(e));
                    } finally {
                        if (lock != null) try {
                            lock.release();
                        } catch (IOException e) {
                        }
                        if (in != null) try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                    if (props != null) {
                        int[] cacheData = (int[]) props.get(P1P_RESULT);
                        Date cacheTime = (Date) props.get(P1P_LAST_MODIFIED);
                        Long cacheSeed = (Long) props.get(P1P_RANDOM_SEED);
                        if (!cacheTime.before(inputTime) && cacheSeed != null && cacheSeed.equals(params.getRandomSeed())) {
                            numRealizations2Make = cacheData;
                            householdArchTypesTime = cacheTime;
                            LogUtil.progress(log, "Loaded results from cache file " + cacheFile.getName());
                        }
                    }
                }
                if (numRealizations2Make == null) {
                    FileOutputStream fout = null;
                    FileLock lock = null;
                    boolean locked = false;
                    try {
                        fout = new FileOutputStream(cacheFile);
                        lock = fout.getChannel().tryLock();
                        locked = (lock != null);
                    } catch (IOException e) {
                        LogUtil.cr(log);
                        log.warning("Unable to write phase-1 cache file: " + ObjectUtil.getMessage(e));
                        if (lock != null) try {
                            lock.release();
                        } catch (IOException e1) {
                        }
                        if (fout != null) {
                            try {
                                fout.close();
                                locked = true;
                            } catch (IOException e1) {
                            }
                        }
                    }
                    try {
                        if (locked) {
                            numRealizations2Make = computeMultiplier();
                            if (fout != null) {
                                HashMap<String, Object> props = new HashMap<String, Object>();
                                props.put(P1P_RESULT, numRealizations2Make);
                                props.put(P1P_LAST_MODIFIED, inputTime);
                                props.put(P1P_RANDOM_SEED, params.getRandomSeed());
                                ObjectOutputStream out = null;
                                try {
                                    out = new ObjectOutputStream(fout);
                                    out.writeObject(props);
                                } catch (Exception e) {
                                    LogUtil.cr(log);
                                    log.warning("Unable to write phase-1 cache file: " + ObjectUtil.getMessage(e));
                                }
                            }
                        }
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException e1) {
                            }
                        }
                    }
                }
                if (numRealizations2Make == null) {
                    try {
                        cacheFileLock.wait(2000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return numRealizations2Make;
    }
