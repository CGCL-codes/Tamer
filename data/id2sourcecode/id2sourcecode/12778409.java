    @SuppressWarnings("unchecked")
    public void searchMBsOnProject() throws MalformedURLException, Exception {
        List<String> resultBeans = null;
        List<String> resultBCs = null;
        String packageName = null;
        String varPackage = null;
        String varMB = null;
        List<Element> elements = null;
        List<ManagedBeanHelper> mbHelperList = null;
        elements = XMLReader.readXML(facesXml, null, null);
        if (elements != null && elements.size() > 0) {
            packageName = EclipseUtil.getPackageFullName("managedbean");
            varPackage = EclipseUtil.getPackageFullName(".bean");
            resultBeans = FileUtil.findJavaFiles(varPackage);
            varPackage = EclipseUtil.getPackageFullName("business");
            resultBCs = FileUtil.findJavaFiles(varPackage);
            if (mbHelperList == null) {
                mbHelperList = new ArrayList<ManagedBeanHelper>();
            }
            for (Element elem : elements) {
                if (elem.getName().compareTo("managed-bean") == 0) {
                    ManagedBeanHelper mb = new ManagedBeanHelper();
                    ;
                    mb.setAbsolutePath(EclipseUtil.getSourceLocation() + "/" + packageName.replace(".", "/") + "/");
                    mb.setPackageName(packageName);
                    List<Element> children = elem.getChildren();
                    for (Element child : children) {
                        String elName = child.getName();
                        String elText = child.getText();
                        if (elName.compareTo("managed-bean-name") == 0) {
                            mb.setVarName(elText);
                            mb.setName(elText.replaceFirst(elText.substring(0, 1), elText.substring(0, 1).toUpperCase()));
                            varMB = elText.substring(0, elText.lastIndexOf("MB"));
                            varMB = varMB.replaceFirst(varMB.substring(0, 1), varMB.substring(0, 1).toUpperCase());
                            for (String bean : resultBeans) {
                                mb.setPojos(new ArrayList<ClassRepresentation>());
                                if (bean.contains(varMB + ".java")) {
                                    bean = bean.replace("/", ".");
                                    bean = bean.substring(0, bean.lastIndexOf(".java"));
                                    mb.getPojos().add(new ClassRepresentation(bean));
                                    break;
                                }
                            }
                            for (String bc : resultBCs) {
                                if (bc.contains(varMB + "BC.java")) {
                                    bc = bc.replace("/", ".");
                                    bc = bc.substring(0, bc.lastIndexOf(".java"));
                                    mb.setBusinessController(new ClassRepresentation(bc));
                                    break;
                                }
                            }
                        }
                        if (elName.compareTo("managed-bean-scope") == 0) {
                            mb.setScope(elText);
                        }
                        if (elName.compareTo("managed-bean-class") == 0) {
                            try {
                                Method[] methodList = EclipseUtil.getDeclaredMethodsFromClass(elText);
                                mb.setActions(new ArrayList<MethodHelper>());
                                for (Method method : methodList) {
                                    MethodHelper action = new MethodHelper();
                                    action.setName(method.getName());
                                    action.setReturnType(new ClassRepresentation(elText));
                                    action.setBody("return TODO;");
                                    action.setReturnContent("TODO");
                                    mb.getActions().add(action);
                                }
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                                return;
                            }
                        }
                    }
                    mb.setReadOnly(false);
                    mbHelperList.add(mb);
                }
            }
        }
        if (mbHelperList != null && !mbHelperList.isEmpty()) {
            Configurator reader = new Configurator();
            reader.writeBeans(mbHelperList, getXml(), false);
        }
    }
