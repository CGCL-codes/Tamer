    public static void main(String arg[]) {
        boolean saveToDatabase = false;
        ApplicationProperties props = ApplicationProperties.get();
        if (saveToDatabase) {
            props.setDatabase("itc-connected");
        } else {
            props.setDatabase("itc-disconnected");
        }
        int seconds = 20;
        long seed = 181282L;
        File folderOrFile = toAbsFile(new File("track2"));
        props.setMaxNodes(15000);
        props.setFinalDepth(4);
        props.setDepthMultiplier(1.5);
        for (int a = 0; a < arg.length; a++) {
            if (arg[a].equalsIgnoreCase("-f")) {
                folderOrFile = toAbsFile(new File(arg[a + 1]));
            }
            if (arg[a].equalsIgnoreCase("-time")) {
                seconds = Integer.valueOf(arg[a + 1]);
            }
            if (arg[a].equalsIgnoreCase("-seed")) {
                seed = Long.valueOf(arg[a + 1]);
            }
            if (arg[a].equalsIgnoreCase("-maxNodes")) {
                props.setMaxNodes(Integer.valueOf(arg[a + 1]));
            }
            if (arg[a].equalsIgnoreCase("-finalDepth")) {
                props.setFinalDepth(Integer.valueOf(arg[a + 1]));
            }
            if (arg[a].equalsIgnoreCase("-depthMultiplier")) {
                props.setDepthMultiplier(Double.valueOf(arg[a + 1]).longValue());
            }
        }
        if (folderOrFile == null) {
            GLog.error("Can't find .tim files! You specified directory or file was: " + folderOrFile);
            GLog.error("Please specify correct working directory via: -Duser.dir=<userdir>");
            return;
        }
        File[] files;
        String folder;
        if (folderOrFile.isDirectory()) {
            files = folderOrFile.listFiles();
            folder = folderOrFile.getAbsolutePath();
        } else {
            files = new File[] { folderOrFile };
            folder = folderOrFile.getParent();
        }
        Arrays.sort(files);
        int counter = 0;
        for (File f : files) {
            if (!f.getName().endsWith(".tim")) {
                GLog.log("Skipped file (not a .tim file):" + f);
                continue;
            }
            if (!f.exists()) {
                GLog.log("Skipped file (does not exist):" + f);
                continue;
            }
            counter++;
            long start = System.currentTimeMillis();
            System.gc();
            String file = folder + File.separatorChar + f.getName().split(".tim")[0];
            GLog.log("Process file:" + file);
            Track2 track2Obj = new Track2(seed);
            try {
                GLog.setLevel(GLog.DEBUG);
                GLog.setOutput(GLog.OutputType.CONSOLE);
                track2Obj.parse(file);
                if (saveToDatabase) {
                    track2Obj.save();
                    break;
                }
                List<? extends ITimeInterval> optimizedTIs = track2Obj.optimize(seconds);
                if (optimizedTIs != null) {
                    track2Obj.writeToFile(optimizedTIs, file);
                } else {
                    GLog.warn("Couldn't write file. No subjects available.");
                }
            } catch (InterruptedException ex) {
                GLog.warn("Waiting for optimization was interrupted.", ex);
            } catch (FileNotFoundException ex) {
                GLog.warn("Couldn't find file:" + file, ex);
            } catch (IOException ex) {
                GLog.warn("IOException while parsing.", ex);
            } catch (DBException ex) {
                GLog.warn("DBException while parsing.", ex);
            }
            GLog.debug2("Total Time (with read and write): " + (System.currentTimeMillis() - start) / 1000f + " seconds necessary \tfor: " + file);
        }
    }
