    public void searchDAOsOnProject() {
        List<String> resultDAOs = null;
        List<String> resultBeans = null;
        String varPackage = null;
        List<DAOHelper> daoHelperList = null;
        String varImpl = null;
        varImpl = ".implementation";
        varPackage = EclipseUtil.getPackageFullName(".bean");
        resultBeans = FileUtil.findJavaFiles(varPackage);
        varPackage = EclipseUtil.getPackageFullName("dao");
        resultDAOs = FileUtil.findJavaFiles(varPackage + varImpl);
        if (resultDAOs != null) {
            for (String dao : resultDAOs) {
                if (!dao.contains("package-info")) {
                    dao = dao.replace("/", ".");
                    dao = dao.substring(dao.indexOf(varImpl) + varImpl.length() + 1, dao.lastIndexOf("DAO.java"));
                    for (String bean : resultBeans) {
                        if (bean.contains(dao + ".java")) {
                            bean = bean.replace("/", ".");
                            bean = bean.substring(0, bean.lastIndexOf(".java"));
                            if (daoHelperList == null) {
                                daoHelperList = new ArrayList<DAOHelper>();
                            }
                            DAOHelper daoH;
                            daoH = new DAOHelper();
                            daoH.setPojo(new ClassRepresentation(bean));
                            daoH.setAbsolutePath(EclipseUtil.getSourceLocation() + "/" + varPackage.replace(".", "/") + "/");
                            daoH.setPackageName(varPackage);
                            daoHelperList.add(daoH);
                        }
                    }
                }
            }
        }
        if (daoHelperList != null && !daoHelperList.isEmpty()) {
            Configurator reader = new Configurator();
            reader.writeDaos(daoHelperList, getXml(), false);
        }
    }
