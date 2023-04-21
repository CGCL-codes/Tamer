    public void install(File f) {
        Map<String, String> manifest = FileUtils.parseManifestFile(f);
        if (manifest == null || !manifest.containsKey(IRONRHINO_COMPONENT_ID)) {
            throw new RuntimeException("invalid component");
        }
        Component newcomp = new Component(manifest);
        Component oldcomp = null;
        for (Component c : getInstalledComponents()) if (c.getId().equals(newcomp.getId())) {
            oldcomp = c;
            break;
        }
        checkDependence(newcomp.getDependence());
        if (oldcomp != null) {
            if (oldcomp.getVersion().compareTo(newcomp.getVersion()) >= 0) throw new RuntimeException("component has installed");
            try {
                new File(oldcomp.getRealPath() + ".bak").delete();
                org.apache.commons.io.FileUtils.moveFile(new File(oldcomp.getRealPath()), new File(oldcomp.getRealPath() + ".bak"));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            oldcomp.setRealPath(oldcomp.getRealPath() + ".bak");
            getBackupedComponents().add(oldcomp);
        }
        String newfilename = new StringBuilder(directory).append(File.separator).append(newcomp.getId()).append("-").append(newcomp.getVersion()).append(".jar").toString();
        try {
            org.apache.commons.io.FileUtils.copyFile(f, new File(newfilename));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        newcomp.setRealPath(newfilename);
        addInstalledComponent(newcomp);
    }
