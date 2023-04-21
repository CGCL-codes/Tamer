    public void write(List<DAOHelper> daos) throws Exception {
        if (daos != null) {
            try {
                this.setInsert(!this.hasDAO());
                for (Iterator<?> iterator = daos.iterator(); iterator.hasNext(); ) {
                    DAOHelper dao = (DAOHelper) iterator.next();
                    if (!dao.isReadOnly()) {
                        ClassHelper clazzInterface = generateInterface(dao);
                        FileUtil.writeClassFile(dao.getAbsolutePath(), clazzInterface, true, true);
                        dao.setDaoInterface(new ClassRepresentation(dao.getInterfaceFullName()));
                        File dir = new File(dao.getAbsolutePathImpl());
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        ClassHelper clazzImpl = generateImplementation(dao);
                        FileUtil.writeClassFile(dao.getAbsolutePathImpl(), clazzImpl, true, false);
                    }
                }
                Configurator reader = new Configurator();
                reader.writeDaos(daos, xml, insert);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
