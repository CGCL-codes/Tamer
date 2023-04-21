    public void parse(ServletContext servletContext) throws SAXException, IOException {
        Logger log = Logger.getLogger(this.getClass());
        try {
            Digester hibernateMappingDigester = new Digester();
            hibernateMappingDigester.addObjectCreate("hibernate-mapping", "org.jcompany.jdoc.mapping.PlcHibernateMappingRoot");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class", "org.jcompany.jdoc.mapping.PlcHibernateClass");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class", "table", "table");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/id", "org.jcompany.jdoc.mapping.PlcHibernateID");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id", "type", "type");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id", "length", "length");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id", "unsaved-value", "unsavedValue");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/id/generator", "org.jcompany.jdoc.mapping.PlcHibernateGenerator");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/id/generator", "class", "className");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/id/generator/param", "org.jcompany.jdoc.mapping.PlcHibernateParam");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/id/generator/param", "addHibernateParam");
            hibernateMappingDigester.addBeanPropertySetter("hibernate-mapping/class/id/generator/param", "param");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/id/generator", "addHibernateGenerator");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/id", "addHibernateID");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/discriminator", "org.jcompany.jdoc.mapping.PlcHibernateDiscriminator");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/discriminator", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/discriminator", "type", "type");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/discriminator", "length", "length");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/discriminator", "addHibernateDiscriminator");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/version", "org.jcompany.jdoc.mapping.PlcHibernateVersion");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/version", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/version", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/version", "type", "type");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/version", "addHibernateVersion");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/property", "org.jcompany.jdoc.mapping.PlcHibernateMappingProperty");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/property", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/property", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/property", "type", "type");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/property", "not-null", "notNull");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/property", "addHibernateMapppingProperty");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/many-to-one", "org.jcompany.jdoc.mapping.PlcHibernateManyToOne");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/many-to-one", "class", "class");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/many-to-one", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/many-to-one", "name", "name");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/many-to-one", "addHibernateManyToOne");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/set", "org.jcompany.jdoc.mapping.PlcHibernateSet");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/set", "role", "role");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/set", "table", "table");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/set/key", "org.jcompany.jdoc.mapping.PlcHibernateKey");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/set/key", "column", "column");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/set/key", "addHibernateKey");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/set/key/one-to-many", "org.jcompany.jdoc.mapping.PlcHibernateOneToMany");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/set/key/one-to-many", "class", "className");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/set/key/one-to-many", "addHibernateOneToMany");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/set", "addHibernateSet");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/subclass", "org.jcompany.jdoc.mapping.PlcHibernateSubClass");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass", "discriminator-value", "discriminatorValue");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/subclass/many-to-one", "org.jcompany.jdoc.mapping.PlcHibernateManyToOne");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/many-to-one", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/many-to-one", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/many-to-one", "class", "className");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/subclass/many-to-one", "addHibernateManyToOne");
            hibernateMappingDigester.addObjectCreate("hibernate-mapping/class/subclass/property", "org.jcompany.jdoc.mapping.PlcHibernateMappingProperty");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "name", "name");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "type", "type");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "column", "column");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "not-null", "notNull");
            hibernateMappingDigester.addSetProperties("hibernate-mapping/class/subclass/property", "length", "length");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/subclass/property", "addHibernateMappingProperty");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class/subclass", "addHibernateSubclass");
            hibernateMappingDigester.addSetNext("hibernate-mapping/class", "addHibernateClass");
            PlcHibernateConfigDigester plcHibernateConfigDigester = new PlcHibernateConfigDigester(servletContext);
            PlcHibernateConfig PlcHibernateConfig = plcHibernateConfigDigester.getHibernateConfig();
            int size = 1;
            if (PlcHibernateConfig.getHibernateSessionFactories() != null) size = ((Vector) PlcHibernateConfig.getHibernateSessionFactories()).size();
            for (int x = 0; x < size; x++) {
                PlcHibernateSessionFactory factory = (PlcHibernateSessionFactory) PlcHibernateConfig.getHibernateSessionFactories().get(x);
                Enumeration mappingFiles = factory.getHibernateMappings().elements();
                Vector fileNames = new Vector();
                while (mappingFiles.hasMoreElements()) {
                    PlcHibernateMapping mapping = (PlcHibernateMapping) mappingFiles.nextElement();
                    fileNames.add(mapping.getResource());
                }
                for (int j = 0; j < fileNames.size(); j++) {
                    StringBuffer packageVO = new StringBuffer((String) servletContext.getInitParameter("packageVO"));
                    String servidor = servletContext.getInitParameter("servidor").trim();
                    InputStream is;
                    if (servidor.equals("silverstream")) {
                        ClassLoader loader = this.getClass().getClassLoader();
                        URL url = loader.getResource(this.montaPath(packageVO) + "/" + fileNames.get(j));
                        is = url.openStream();
                    } else {
                        is = servletContext.getResourceAsStream("/WEB-INF/classes/" + fileNames.get(j));
                    }
                    if (is != null) {
                        PlcHibernateMappingRoot hibernateMappingRoot = (PlcHibernateMappingRoot) hibernateMappingDigester.parse(is);
                        mappingsTable.put(fileNames.get(j), hibernateMappingRoot);
                    } else {
                        log.warn("jDoc - Nao conseguiu localizar os arquivos de mapeamento hibernate (*.hbm.xml)");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro no digester da Hibernate " + e);
            e.printStackTrace();
        }
    }
