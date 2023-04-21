    @Override
    public Boolean doInBackground() {
        boolean result = true;
        mainNodesHash = new Hashtable<String, DefaultMutableTreeNode>();
        secondaryNodesHash = new Hashtable<String, DefaultMutableTreeNode>();
        DefaultMutableTreeNode currentMainNode = topTreeNode;
        DefaultMutableTreeNode currentSecondaryNode;
        TLE currentTLE = null;
        satCount = 0;
        if (loadTLEfromWeb && userSelectedNoToDownload) {
            return new Boolean(result);
        }
        for (int i = 0; i < tleDownloader.fileNames.length; i++) {
            if (mainNodesHash.containsKey(tleDownloader.primCat[i])) {
                currentMainNode = mainNodesHash.get(tleDownloader.primCat[i]);
            } else {
                currentMainNode = new DefaultMutableTreeNode(tleDownloader.primCat[i]);
                mainNodesHash.put(tleDownloader.primCat[i], currentMainNode);
                topTreeNode.add(currentMainNode);
            }
            currentSecondaryNode = new DefaultMutableTreeNode(tleDownloader.secondCat[i]);
            currentMainNode.add(currentSecondaryNode);
            secondaryNodesHash.put(tleDownloader.secondCat[i], currentSecondaryNode);
            try {
                BufferedReader tleReader = null;
                if (!loadTLEfromWeb) {
                    File tleFile = new File(tleDownloader.getTleFilePath(i));
                    FileReader tleFileReader = new FileReader(tleFile);
                    tleReader = new BufferedReader(tleFileReader);
                } else {
                    if (!dialog.isVisible()) {
                        result = false;
                        return new Boolean(result);
                    }
                    URL url = new URL(tleDownloader.getTleWebPath(i));
                    URLConnection c = url.openConnection();
                    InputStreamReader isr = new InputStreamReader(c.getInputStream());
                    tleReader = new BufferedReader(isr);
                    publish(new ProgressStatus((int) Math.round((i * 100.0) / tleDownloader.fileNames.length), tleDownloader.fileNames[i]));
                }
                String nextLine = null;
                while ((nextLine = tleReader.readLine()) != null) {
                    currentTLE = new TLE(nextLine, tleReader.readLine(), tleReader.readLine());
                    tleHash.put(currentTLE.getSatName(), currentTLE);
                    currentSecondaryNode.add(new DefaultMutableTreeNode(currentTLE.getSatName()));
                    satCount++;
                }
                tleReader.close();
            } catch (Exception e) {
                System.out.println("ERROR IN TLE READING POSSIBLE FILE FORMAT OR MISSING TLE FILES:" + e.toString());
                result = false;
                return new Boolean(result);
            }
        }
        try {
            File userTLdir = new File(usrTLEpath);
            if (userTLdir.isDirectory()) {
                IOFileFilter tleFilter = new IOFileFilter("txt", "tle", "dat");
                File[] tleFiles = userTLdir.listFiles(tleFilter);
                for (File f : tleFiles) {
                    String fn = f.getName();
                    Boolean r = loadTLEDataFile(f, "Custom", fn.substring(0, fn.length() - 4), false);
                    if (!r) {
                        System.out.println("Error loading TLE file: " + f.getCanonicalPath());
                    }
                }
            } else {
                System.out.println("ERROR: User TLE folder path is not a directory.");
            }
        } catch (Exception e) {
            System.out.println("Error loading user supplied TLE data files:" + e.toString());
        }
        return new Boolean(result);
    }
