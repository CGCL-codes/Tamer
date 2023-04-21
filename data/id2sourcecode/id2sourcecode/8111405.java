    public void createDeploymentDescriptor(String organisationID, File deploymentDescriptorFile, File templateFile, Map<String, String> additionalVariables, DeployOverwriteBehaviour deployOverwriteBehaviour) throws IOException {
        JFireServerConfigModule cfMod = mcf.getConfigModule();
        DatabaseCf dbCf = cfMod.getDatabase();
        if (!deploymentDescriptorFile.isAbsolute()) {
            deploymentDescriptorFile = new File(new File(cfMod.getJ2ee().getJ2eeDeployBaseDirectory()).getAbsoluteFile().getParentFile(), deploymentDescriptorFile.getPath());
        }
        if (deploymentDescriptorFile.exists()) {
            switch(deployOverwriteBehaviour) {
                case EXCEPTION:
                    throw new DeployedFileAlreadyExistsException(deploymentDescriptorFile);
                case KEEP:
                    logger.warn("File " + deploymentDescriptorFile + " already exists. Will not change anything!");
                    return;
                case OVERWRITE:
                    logger.warn("File " + deploymentDescriptorFile + " already exists. Will overwrite this file!");
                    break;
                default:
                    throw new IllegalStateException("Unknown deployOverwriteBehaviour: " + deployOverwriteBehaviour);
            }
        }
        String databaseName = createDatabaseName(organisationID);
        String dbURL = dbCf.getDatabaseURL(databaseName);
        String datasourceJNDIName_relative = OrganisationCf.DATASOURCE_PREFIX_RELATIVE + organisationID;
        String datasourceJNDIName_absolute = OrganisationCf.DATASOURCE_PREFIX_ABSOLUTE + organisationID;
        String jdoPersistenceManagerFactoryJNDIName_relative = OrganisationCf.PERSISTENCE_MANAGER_FACTORY_PREFIX_RELATIVE + organisationID;
        String jdoPersistenceManagerFactoryJNDIName_absolute = OrganisationCf.PERSISTENCE_MANAGER_FACTORY_PREFIX_ABSOLUTE + organisationID;
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("organisationID", organisationID);
        variables.put("datasourceJNDIName_relative_noTx", datasourceJNDIName_relative + "/no-tx");
        variables.put("datasourceJNDIName_absolute_noTx", datasourceJNDIName_absolute + "/no-tx");
        variables.put("datasourceJNDIName_relative_localTx", datasourceJNDIName_relative + "/local-tx");
        variables.put("datasourceJNDIName_absolute_localTx", datasourceJNDIName_absolute + "/local-tx");
        variables.put("datasourceJNDIName_relative_xa", datasourceJNDIName_relative + "/xa");
        variables.put("datasourceJNDIName_absolute_xa", datasourceJNDIName_absolute + "/xa");
        variables.put("datasourceMetadataTypeMapping", dbCf.getDatasourceMetadataTypeMapping());
        variables.put("jdoPersistenceManagerFactoryJNDIName_relative", jdoPersistenceManagerFactoryJNDIName_relative);
        variables.put("jdoPersistenceManagerFactoryJNDIName_absolute", jdoPersistenceManagerFactoryJNDIName_absolute);
        variables.put("databaseDriverName_noTx", dbCf.getDatabaseDriverName_noTx());
        variables.put("databaseDriverName_localTx", dbCf.getDatabaseDriverName_localTx());
        variables.put("databaseDriverName_xa", dbCf.getDatabaseDriverName_xa());
        variables.put("databaseURL", dbURL);
        variables.put("databaseName", databaseName);
        variables.put("databaseUserName", dbCf.getDatabaseUserName());
        variables.put("databasePassword", dbCf.getDatabasePassword());
        variables.put("deploymentDescriptorDirectory", deploymentDescriptorFile.getParent());
        variables.put("deploymentDescriptorDirectory_absolute", deploymentDescriptorFile.getParent());
        variables.put("deploymentDescriptorDirectory_relative", IOUtil.getRelativePath(new File("."), deploymentDescriptorFile.getParent()));
        variables.put("deploymentDescriptorFileName", deploymentDescriptorFile.getName());
        if (additionalVariables != null) variables.putAll(additionalVariables);
        _createDeploymentDescriptor(deploymentDescriptorFile, templateFile, variables);
    }
