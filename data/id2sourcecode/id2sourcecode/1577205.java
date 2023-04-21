    private static void loadConfiguration(String filename) throws Exception {
        File file = new File(filename);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            int b = 0;
            while ((b = fileInputStream.read()) > 0) outputStream.write(b);
        } finally {
            fileInputStream.close();
        }
        XMLKeeper c = new XMLKeeper(outputStream.toByteArray());
        String stat = c.getNodeValueByPath("configuration.usage-stat");
        if ((stat == null) || stat.isEmpty() || stat.equalsIgnoreCase("false") || stat.equalsIgnoreCase("off")) submitUsageStat = false;
        String syncObjectsPath = "configuration.sync-objects.sync";
        int syncObjectsCount = c.getNodesCountByPath(syncObjectsPath) + (c.contains(syncObjectsPath) ? 1 : 0);
        for (int f = 0; f < syncObjectsCount; f++) {
            String syncObjectPath = syncObjectsPath + (f > 0 ? f : "");
            SyncObject syncObject = new SyncObject();
            syncObject.name = c.getNodeValueByPath(syncObjectPath + ".name");
            String dataSourcePath = syncObjectPath + ".datasource";
            DataSource dataSource = new DataSource();
            String name = c.getNodeValueByPath(dataSourcePath + ".name");
            String driver = c.getNodeValueByPath(dataSourcePath + ".driver");
            String url = c.getNodeValueByPath(dataSourcePath + ".url");
            dataSource.setName(name);
            dataSource.setDriver(driver);
            dataSource.setUrl(url);
            if (c.contains(dataSourcePath + ".properties")) {
                Properties properties = new Properties();
                String propertiesPath = dataSourcePath + ".properties.property";
                int pcount = c.getNodesCountByPath(propertiesPath) + (c.contains(propertiesPath) ? 1 : 0);
                for (int n = 0; n < pcount; n++) {
                    String properyPath = propertiesPath + (n > 0 ? n : "");
                    String pname = c.getNodeValueByPath(properyPath + ".name");
                    String pvalue = c.getNodeValueByPath(properyPath + ".value");
                    properties.put(pname, pvalue);
                }
                dataSource.setProperties(properties);
            }
            syncObject.dataSource = dataSource;
            syncObject.workingCopy = c.getNodeValueByPath(syncObjectPath + ".working-copy");
            syncObject.repositoryPath = c.getNodeValueByPath(syncObjectPath + ".repository-path");
            syncObject.userName = c.getNodeValueByPath(syncObjectPath + ".auth.name");
            syncObject.password = c.getNodeValueByPath(syncObjectPath + ".auth.password");
            syncObject.query = c.getNodeValueByPath(syncObjectPath + ".datasource.query.<cdata>");
            syncObject.nameColumn = c.getNodeValueByPath(syncObjectPath + ".datasource.name-column-name");
            syncObject.typeColumn = c.getNodeValueByPath(syncObjectPath + ".datasource.type-column-name");
            syncObject.ownerColumn = c.getNodeValueByPath(syncObjectPath + ".datasource.owner-column-name");
            syncObject.dateColumn = c.getNodeValueByPath(syncObjectPath + ".datasource.date-column-name");
            syncObjects.add(syncObject);
        }
    }
