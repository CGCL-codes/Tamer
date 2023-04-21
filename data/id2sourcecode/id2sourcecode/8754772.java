    public void parse(ServletContext servletContext, String nomeArquivo) throws IOException, SAXException {
        if (log.isDebugEnabled()) log.debug("Entrou para fazer parse do arquivo " + nomeArquivo);
        Digester hibernateConfigDigester = new Digester();
        hibernateConfigDigester.addObjectCreate("hibernate-configuration", PlcHibernateConfig.class);
        hibernateConfigDigester.addObjectCreate("hibernate-configuration/session-factory", PlcHibernateSessionFactory.class);
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory", "name", "name");
        hibernateConfigDigester.addObjectCreate("hibernate-configuration/session-factory/property", PlcHibernateProperty.class);
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/property", "name", "propertyName");
        hibernateConfigDigester.addBeanPropertySetter("hibernate-configuration/session-factory/property", "propertyValue");
        hibernateConfigDigester.addSetNext("hibernate-configuration/session-factory/property", "addHibernateProperty");
        hibernateConfigDigester.addObjectCreate("hibernate-configuration/session-factory/mapping", PlcHibernateMapping.class);
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/mapping", "resource", "resource");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/mapping", "file", "file");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/mapping", "jar", "jar");
        hibernateConfigDigester.addSetNext("hibernate-configuration/session-factory/mapping", "addHibernateMapping");
        hibernateConfigDigester.addObjectCreate("hibernate-configuration/session-factory/jcs-class-cache", PlcHibernateJcsClassCache.class);
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-class-cache", "class", "class");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-class-cache", "region", "region");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-class-cache", "usage", "usage");
        hibernateConfigDigester.addSetNext("hibernate-configuration/session-factory/jcs-class-cache", "addHibernateJcsClassCache");
        hibernateConfigDigester.addObjectCreate("hibernate-configuration/session-factory/jcs-collection-cache", PlcHibernateJcsCollectionCache.class);
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-collection-cache", "collection", "collection");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-collection-cache", "region", "region");
        hibernateConfigDigester.addSetProperties("hibernate-configuration/session-factory/jcs-collection-cache", "usage", "usage");
        hibernateConfigDigester.addSetNext("hibernate-configuration/session-factory/jcs-collection-cache", "addHibernateJcsCollectionCache");
        hibernateConfigDigester.addSetNext("hibernate-configuration/session-factory", "addHibernateSessionFactory");
        String servidor = servletContext.getInitParameter("servidor").trim();
        InputStream is;
        if (log.isDebugEnabled()) log.debug("jDoc - Vai testar o arquivo " + nomeArquivo);
        if (!nomeArquivo.endsWith(".cfg.xml")) nomeArquivo = nomeArquivo + ".cfg.xml";
        if (log.isDebugEnabled()) log.debug("jDoc - Apos o teste o arquivo ficou " + nomeArquivo);
        if (servidor.equals("silverstream")) {
            ClassLoader loader = this.getClass().getClassLoader();
            URL url = loader.getResource(nomeArquivo);
            is = url.openStream();
        } else {
            try {
                is = servletContext.getResourceAsStream("/WEB-INF/classes/" + nomeArquivo);
            } catch (Exception e) {
                is = servletContext.getResourceAsStream("\\WEB-INF\\classes\\" + nomeArquivo);
            }
        }
        if (is != null) {
            try {
                hibernateConfig = (PlcHibernateConfig) hibernateConfigDigester.parse(is);
            } catch (Exception e) {
                log.error("Erro ao tentar fazer parse nos arquivos da Hibernate " + e, e);
                e.printStackTrace();
            }
            hibernateConfigsVector.add(hibernateConfig);
        } else {
            log.warn("jDoc - Nao conseguiu recuperar arquivo de configuracao hibernate: " + nomeArquivo);
        }
    }
