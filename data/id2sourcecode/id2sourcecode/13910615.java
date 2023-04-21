    public void searchBCsOnProject() {
        List<String> resultBCs = null;
        List<String> resultDAOs = null;
        String varPackage = null;
        String packageName = null;
        String varImpl = null;
        List<BusinessControllerHelper> bcHelperList = null;
        varImpl = ".implementation";
        packageName = EclipseUtil.getPackageFullName("business");
        resultBCs = FileUtil.findJavaFiles(packageName + varImpl);
        varPackage = EclipseUtil.getPackageFullName("dao");
        resultDAOs = FileUtil.findJavaFiles(varPackage + varImpl);
        if (resultBCs != null) {
            for (String bc : resultBCs) {
                if (!bc.contains("package-info")) {
                    bc = bc.replace("/", ".");
                    bc = bc.substring(bc.indexOf(varImpl) + varImpl.length() + 1, bc.lastIndexOf("BC.java"));
                    for (String dao : resultDAOs) {
                        if (dao.contains(bc + "DAO.java")) {
                            dao = dao.replace("/", ".");
                            dao = dao.substring(0, dao.lastIndexOf(".java"));
                            if (bcHelperList == null) {
                                bcHelperList = new ArrayList<BusinessControllerHelper>();
                            }
                            BusinessControllerHelper bcH;
                            bcH = new BusinessControllerHelper();
                            bcH.setName(bc);
                            bcH.setDaoClass(new ClassRepresentation(dao));
                            bcH.setAbsolutePath(EclipseUtil.getSourceLocation() + "/" + varPackage.replace(".", "/") + "/");
                            bcH.setPackageName(packageName);
                            bcHelperList.add(bcH);
                        }
                    }
                }
            }
        }
        if (bcHelperList != null && !bcHelperList.isEmpty()) {
            Configurator reader = new Configurator();
            reader.writeBcs(bcHelperList, getXml(), false);
        }
    }
