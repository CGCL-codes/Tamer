    public static Project loadProject(URL url) throws IOException, Exception {
        Project p = null;
        String urlString = url.toString();
        int lastDot = urlString.lastIndexOf(".");
        String suffix = "";
        if (lastDot >= 0) {
            suffix = urlString.substring(lastDot).toLowerCase();
        }
        if (suffix.equals(".xmi")) {
            p = new Project();
            XMIParser.SINGLETON.readModels(p, url);
            MModel model = XMIParser.SINGLETON.getCurModel();
            UmlHelper.getHelper().addListenersToModel(model);
            p._UUIDRefs = XMIParser.SINGLETON.getUUIDRefs();
            try {
                p.addMember(model);
                p.setNeedsSave(false);
            } catch (PropertyVetoException pve) {
            }
            org.argouml.application.Main.addPostLoadAction(new ResetStatsLater());
        } else if ((suffix.equals(COMPRESSED_FILE_EXT)) || (suffix.equals(BOTL_FILE_EXT))) {
            try {
                ZipInputStream zis = new ZipInputStream(url.openStream());
                String name = zis.getNextEntry().getName();
                while (!name.endsWith(PROJECT_FILE_EXT)) {
                    name = zis.getNextEntry().getName();
                }
                System.out.println("ARGO NAME: " + name);
                ArgoParser.SINGLETON.setURL(url);
                ArgoParser.SINGLETON.readProject(zis, false);
                p = ArgoParser.SINGLETON.getProject();
                zis.close();
                if (suffix.equals(BOTL_FILE_EXT)) {
                    p.setBOTLProject(true);
                } else {
                    p.setBOTLProject(false);
                }
            } catch (Exception e) {
                cat.error("Oops, something went wrong in Project.loadProject ");
                cat.error(e);
                throw e;
            }
            try {
                p.loadZippedProjectMembers(url);
            } catch (IOException e) {
                cat.error("Project file corrupted");
                cat.error(e);
                throw e;
            }
            p.postLoad();
        } else {
            ArgoParser.SINGLETON.readProject(url);
            p = ArgoParser.SINGLETON.getProject();
            p.loadAllMembers();
            p.postLoad();
        }
        return p;
    }
