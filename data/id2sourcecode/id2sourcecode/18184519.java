    public void loadZippedProjectMembers(URL url) throws IOException, PropertyVetoException {
        loadModelFromXMI(url);
        try {
            PGMLParser.SINGLETON.setOwnerRegistry(_UUIDRefs);
            ZipInputStream zis = new ZipInputStream(url.openStream());
            SubInputStream sub = new SubInputStream(zis);
            ZipEntry currentEntry = null;
            while ((currentEntry = sub.getNextEntry()) != null) {
                if (currentEntry.getName().endsWith(".pgml")) {
                    Argo.log.info("Now going to load " + currentEntry.getName() + " from ZipInputStream");
                    ArgoDiagram d = (ArgoDiagram) PGMLParser.SINGLETON.readDiagram(sub, false);
                    if (d == null) System.out.println("ERROR: Cannot load diagram " + currentEntry.getName()); else addMember(d);
                    Argo.log.info("Finished loading " + currentEntry.getName());
                }
            }
            zis.close();
        } catch (IOException e) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage(e.toString());
            System.out.println("Oops, something went wrong in Project.loadZippedProjectMembers() " + e);
            e.printStackTrace();
            throw e;
        }
    }
