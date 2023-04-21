        public Options getOptions() {
            return new Options() {

                public String readText(int type) {
                    if (getPackageOptions() == null) return null;
                    Tree tree = getPackageOptions().getFirstChildWithType(type);
                    if (tree == null) return null;
                    tree = tree.getChild(0);
                    if (tree == null) return null;
                    return tree.getText();
                }

                @Override
                public boolean hasVetoableChangeSupport() {
                    String changeSupport = readText(SimplemodelParser.CHANGE_SUPPORT_TYPE);
                    return "both".equals(changeSupport) || "vetoable".equals(changeSupport);
                }

                @Override
                public boolean hasPropertyChangeSupport() {
                    String changeSupport = readText(SimplemodelParser.CHANGE_SUPPORT_TYPE);
                    return "both".equals(changeSupport) || "property".equals(changeSupport);
                }

                @Override
                public LockType getLockType() {
                    String lockType = readText(SimplemodelParser.LOCK_TYPE);
                    if ("reentrant".equals(lockType)) {
                        return LockType.Reentrant;
                    }
                    if ("synchronized".equals(lockType)) {
                        return LockType.Synchronized;
                    }
                    if ("readwrite".equals(lockType)) {
                        return LockType.ReentrantReadWrite;
                    }
                    return LockType.None;
                }

                @Override
                public String getBaseClassName() {
                    return SimplemodelTreeAdaptor.SMPackageTree.this.getPackageName() + "Base" + (getModelType() == ModelType.Interface ? "Impl" : "");
                }

                @Override
                public String getBaseInterfaceName() {
                    return SimplemodelTreeAdaptor.SMPackageTree.this.getPackageName() + "Base";
                }

                @Override
                public String getBaseInterfaceFull() {
                    return (getIPackage().equals("") ? "" : (getIPackage() + ".")) + getBaseInterfaceName();
                }

                @Override
                public String getCPackage() {
                    final String cpackage = readText(SimplemodelParser.CPACKAGE);
                    return cpackage == null ? "" : cpackage;
                }

                @Override
                public String getIPackage() {
                    final String ipackage = readText(SimplemodelParser.IPACKAGE);
                    return ipackage == null ? "" : ipackage;
                }

                @Override
                public ModelType getModelType() {
                    final String modelType = readText(SimplemodelParser.MODEL_TYPE);
                    return "interface".equals(modelType) ? ModelType.Interface : ModelType.Class;
                }

                @Override
                public boolean useOverrideAnnotations() {
                    return getModelType() == ModelType.Interface;
                }

                @Override
                public String getCFactory() {
                    return getPackageName() + "FactoryImpl";
                }

                @Override
                public String getIFactory() {
                    return getPackageName() + "Factory";
                }

                @Override
                public String getIFactoryFull() {
                    return (getIPackage().equals("") ? "" : (getIPackage() + ".")) + getIFactory();
                }

                @Override
                public String getCFactoryFull() {
                    return (getCPackage().equals("") ? "" : (getCPackage() + ".")) + getCFactory();
                }
            };
        }
