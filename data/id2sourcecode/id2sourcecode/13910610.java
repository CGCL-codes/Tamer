    public void write(List<BusinessControllerHelper> bcs) throws Exception {
        if (bcs != null) {
            try {
                this.setInsert(!this.hasBC());
                for (Iterator<?> iterator = bcs.iterator(); iterator.hasNext(); ) {
                    BusinessControllerHelper bc = (BusinessControllerHelper) iterator.next();
                    if (!bc.isReadOnly()) {
                        ClassHelper clazzInterface = generateInterface(bc);
                        FileUtil.writeClassFile(bc.getAbsolutePath(), clazzInterface, false, true);
                        bc.setBcInterface(new ClassRepresentation(bc.getInterfaceFullName()));
                        File dir = new File(bc.getAbsolutePathImpl());
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        ClassHelper clazzImpl = generateImplementation(bc);
                        FileUtil.writeClassFile(bc.getAbsolutePathImpl(), clazzImpl, true, false);
                    }
                }
                Configurator reader = new Configurator();
                reader.writeBcs(bcs, xml, insert);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
