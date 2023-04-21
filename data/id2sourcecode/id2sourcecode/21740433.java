    public String createChangelist(String markup, String imageDir, String imageExt) throws Exception {
        if (outputDir == null || outputDir.length() == 0) throw new Exception("Output directory is not specified!");
        LinkedHashSet workingSet = createWorkingSet(markup, imageDir, imageExt);
        LinkedList changes = getChanges(workingSet, markup, imageDir, imageExt);
        if (workingSet.size() > 0) {
            System.err.println("\nWARNING! Orphaned files:");
            Iterator i = workingSet.iterator();
            while (i.hasNext()) System.err.println("\t" + (String) i.next());
        }
        String pfcFile = null;
        if ("pfc".equals(style)) {
            pfcFile = createPFC(changes, markup, imageDir, imageExt);
            System.out.println("Generated PFC file " + pfcFile);
            Matcher m = subDirFile.matcher(pfcFile);
            m.matches();
            String pkgSubDir = m.group(1);
            String pfcFilename = props.getProperty("changelist.style.pfc.file");
            pfcFile = m.replaceFirst(pkgSubDir + "/" + pfcFilename);
            String packagerPath = props.getProperty("changelist.style.pfc.packager.path");
            if (!packagerPath.endsWith("/")) packagerPath += "/";
            StringBuffer extraDeps = new StringBuffer();
            String prop = "changelist.style.pfc.dependencies." + markup + "_" + imageDir + "_" + imageExt;
            if (props.containsKey(prop)) {
                String _deps = props.getProperty(prop);
                String[] deps = _deps.split(",");
                String outDir = m.group().substring(0, m.start(2));
                String pkgDir = packagerPath + pkgSubDir;
                for (int i = 0; i < deps.length; i++) {
                    Matcher _m = subDirFile.matcher(deps[i]);
                    _m.matches();
                    File out = new File(outDir, _m.group(2));
                    System.err.println("Copying " + deps[i] + "\n        " + out.getPath());
                    try {
                        writeFile(out, readFile(new File(deps[i])));
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    }
                    extraDeps.append(pkgDir + "/" + _m.group(2)).append(",");
                }
            }
            changes.clear();
            StringBuffer changeline = new StringBuffer();
            changeline.append(packagerPath).append(pfcFile).append(",");
            if (extraDeps.length() > 0) changeline.append(extraDeps);
            changeline.append("__IGNORE__");
            String home = props.getProperty("changelist.style.pfc.homepage");
            if (home != null) {
                if (home.endsWith(".mml")) home = home.substring(0, home.length() - 1) + "c";
                changes.add("markup/" + markup + "/" + home + ":" + changeline);
            } else System.out.println("WARNING: PFC style changelist has no home page to give dependency " + pfcFilename);
            home = props.getProperty("changelist.style.pfc.demo_homepage");
            if (home != null) {
                if (home.endsWith(".mml")) home = home.substring(0, home.length() - 1) + "c";
                changes.add("markup/" + markup + "/" + home + ":" + changeline);
            } else System.out.println("WARNING: PFC style changelist has no demo home page to give dependency " + pfcFilename);
        }
        return writeChangelist(changes, markup, imageDir, imageExt);
    }
