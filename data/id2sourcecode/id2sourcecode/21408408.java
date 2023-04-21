    public void writeConstantes(String path, List<NavigationRuleHelper> navRules) throws Exception {
        ClassHelper clazzAlias = new ClassHelper();
        clazzAlias.setName(ALIAS_CLASS_NAV);
        clazzAlias.setPackageName(packageName);
        List<FieldHelper> fields = new ArrayList<FieldHelper>();
        if (navRules != null) {
            try {
                for (NavigationRuleHelper navigationRuleHelper : navRules) {
                    for (NavigationCaseHelper navigationCaseHelper : navigationRuleHelper.getNavCases()) {
                        if (navigationCaseHelper.getFromOutcome() != null) {
                            String name = ALIAS_PREFIX + navigationCaseHelper.getFromOutcome().toUpperCase();
                            if (!findField(fields, name)) {
                                FieldHelper field = new FieldHelper();
                                field.setName(name);
                                field.setValue("\"" + navigationCaseHelper.getFromOutcome() + "\"");
                                field.setModifier(Modifier.PUBLIC + Modifier.STATIC + Modifier.FINAL);
                                field.setType(new ClassRepresentation(String.class.getName()));
                                field.setHasGetMethod(false);
                                field.setHasSetMethod(false);
                                fields.add(field);
                            }
                        }
                    }
                }
                clazzAlias.setFields(fields);
                FileUtil.writeClassFile(path, clazzAlias, false, false);
                Configurator reader = new Configurator();
                reader.writeNavRulePackage(packageName, xml, false);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
