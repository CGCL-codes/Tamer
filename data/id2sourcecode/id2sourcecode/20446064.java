    private void createRepository(String templateName) throws IOException {
        Repository systemRepo = manager.getSystemRepository();
        try {
            String templateFileName = templateName + ".ttl";
            File templatesDir = new File(appConfig.getDataDir(), TEMPLATES_DIR);
            File templateFile = new File(templatesDir, templateFileName);
            InputStream templateStream;
            if (templateFile.exists()) {
                if (!templateFile.canRead()) {
                    writeError("Not allowed to read template file: " + templateFile);
                    return;
                }
                templateStream = new FileInputStream(templateFile);
            } else {
                templateStream = Console.class.getResourceAsStream(templateFileName);
                if (templateStream == null) {
                    writeError("No template called " + templateName + " found in " + templatesDir);
                    return;
                }
            }
            String template = IOUtil.readString(new InputStreamReader(templateStream, "UTF-8"));
            templateStream.close();
            ConfigTemplate configTemplate = new ConfigTemplate(template);
            Map<String, String> valueMap = new HashMap<String, String>();
            Map<String, List<String>> variableMap = configTemplate.getVariableMap();
            if (!variableMap.isEmpty()) {
                writeln("Please specify values for the following variables:");
            }
            for (Map.Entry<String, List<String>> entry : variableMap.entrySet()) {
                String var = entry.getKey();
                List<String> values = entry.getValue();
                write(var);
                if (values.size() > 1) {
                    write(" (");
                    for (int i = 0; i < values.size(); i++) {
                        if (i > 0) {
                            write("|");
                        }
                        write(values.get(i));
                    }
                    write(")");
                }
                if (!values.isEmpty()) {
                    write(" [" + values.get(0) + "]");
                }
                write(": ");
                String value = in.readLine();
                if (value == null) {
                    return;
                }
                value = value.trim();
                if (value.length() == 0) {
                    value = null;
                }
                valueMap.put(var, value);
            }
            String configString = configTemplate.render(valueMap);
            ValueFactory vf = systemRepo.getValueFactory();
            Graph graph = new GraphImpl(vf);
            RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE, vf);
            rdfParser.setRDFHandler(new StatementCollector(graph));
            rdfParser.parse(new StringReader(configString), RepositoryConfigSchema.NAMESPACE);
            Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE, RepositoryConfigSchema.REPOSITORY);
            RepositoryConfig repConfig = RepositoryConfig.create(graph, repositoryNode);
            repConfig.validate();
            if (RepositoryConfigUtil.hasRepositoryConfig(systemRepo, repConfig.getID())) {
                boolean proceed = askProceed("WARNING: you are about to overwrite the configuration of an existing repository!", false);
                if (!proceed) {
                    writeln("Create aborted");
                    return;
                }
            }
            RepositoryConfigUtil.updateRepositoryConfigs(systemRepo, repConfig);
            writeln("Repository created");
        } catch (Exception e) {
            writeError(e.getMessage());
            logger.error("Failed to create repository", e);
        }
    }
