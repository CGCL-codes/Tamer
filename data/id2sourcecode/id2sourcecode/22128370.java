    public void write(List<FacadeHelper> facades) throws Exception {
        if (facades != null) {
            try {
                this.setInsert(!this.hasFacade());
                for (FacadeHelper facade : facades) {
                    if (!facade.isReadOnly()) {
                        ClassHelper clazzImpl = generateImplementation(facade);
                        FileUtil.writeClassFile(facade.getAbsolutePath(), clazzImpl, true, true);
                    }
                }
                Configurator reader = new Configurator();
                reader.writeFacades(facades, xml, insert);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
