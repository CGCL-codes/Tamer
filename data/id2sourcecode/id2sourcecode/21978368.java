    private void doIt() throws Throwable {
        GenericDAO<UserMapOriginal> dao = DAOFactory.createDAO(UserMapOriginal.class);
        try {
            ReadOnlyTransaction.beginTransaction();
        } catch (Throwable e) {
            logger.error(e);
            throw e;
        }
        try {
            UserMapOriginal map = dao.findUniqueByCriteria(Expression.eq("guid", mixMapGuid));
            final File srcDir = new File(Configuration.getInstance().getPrivateMapStorage().toString());
            for (UserMapOriginal m : map.getSubmaps()) {
                final File mapDir = new File(srcDir, m.getGuid());
                if (mapDir.exists() && mapDir.isDirectory()) {
                    for (String fileName : mapDir.list()) {
                        File file = new File(mapDir, fileName);
                        if (file.isFile() && file.exists() && file.canRead()) {
                            while (fileName.indexOf('.') > 0) {
                                fileName = fileName.split("\\.")[0];
                            }
                            if (fileName.indexOf('_') > 0) {
                                if (tileCode.equalsIgnoreCase(fileName)) {
                                    FileUtils.copyFile(file, new File(outDir.getAbsolutePath(), m.getGuid() + ".png"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            logger.error("Error importing", e);
        } finally {
            ReadOnlyTransaction.closeTransaction();
        }
    }
