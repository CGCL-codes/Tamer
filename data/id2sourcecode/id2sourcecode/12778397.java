    public void write(List<ManagedBeanHelper> beans) throws Exception {
        List<Element> list = new ArrayList<Element>();
        if (beans != null) {
            try {
                this.setInsert(!this.hasMB());
                for (Iterator<?> iterator = beans.iterator(); iterator.hasNext(); ) {
                    ManagedBeanHelper bean = (ManagedBeanHelper) iterator.next();
                    if (!bean.isReadOnly()) {
                        ClassHelper clazz = extractManagedBeanClass(bean);
                        FileUtil.writeClassFile(bean.getAbsolutePath(), clazz, true, false);
                    }
                    Element novoMB = new Element(NODE_MANAGED_BEAN);
                    Element mbName = new Element(NODE_MANAGED_BEAN_NAME, novoMB.getNamespace());
                    mbName.setText(bean.getVarName());
                    Element mbClass = new Element(NODE_MANAGED_BEAN_CLASS, novoMB.getNamespace());
                    mbClass.setText(bean.getPackageName().concat(".").concat(CommonsUtil.toUpperCaseFirstLetter(bean.getName())));
                    Element mbScope = new Element(NODE_MANAGED_BEAN_SCOPE, novoMB.getNamespace());
                    mbScope.setText(bean.getScope());
                    novoMB.addContent(0, mbName);
                    novoMB.addContent(1, mbClass);
                    novoMB.addContent(2, mbScope);
                    list.add(novoMB);
                }
                XMLReader.writeXML(BASIC_XML_FACES, facesXml, null, NODE_MANAGED_BEAN, list, insert);
                Configurator reader = new Configurator();
                reader.writeBeans(beans, xml, insert);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
