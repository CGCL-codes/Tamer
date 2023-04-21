    private void loadUpdate(Module module, UpdateId uid) throws IOException {
        Map<UpdateId, ModuleUpdateDescriptor> m = this.moduleUpdatesCache.get(module.getName());
        if (m == null) {
            return;
        }
        ModuleUpdateDescriptor mud = (ModuleUpdateDescriptor) m.get(uid);
        UpdatePartDescriptor[] ucds = mud.getComponents(OSUtils.getOsName());
        UpdatePartDescriptor ucd;
        Stopper stopper = new Stopper() {

            public boolean stop() {
                return cancelUpdate;
            }
        };
        for (int i = 0; i < ucds.length; i++) {
            if (cancelUpdate) {
                return;
            }
            ucd = ucds[i];
            URI nurl = url.resolve(uid.toString() + "/").resolve(ucd.getContentLocation());
            HttpURLConnection conn = (HttpURLConnection) nurl.toURL().openConnection();
            conn.setDoInput(true);
            BufferedInputStream is = new BufferedInputStream((InputStream) conn.getContent());
            UpdatePartInstaller.installUpdatePart(ucd, is, localUpdateRepository, stopper);
            conn.disconnect();
            if (ucd.needsAppRestart()) {
                needToRestartApplication = true;
            }
        }
        mud.setInstalled(true);
    }
