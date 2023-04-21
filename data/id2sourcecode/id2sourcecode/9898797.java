    private static Map getSupportedAttributesForXgrl(String name, Node parent) {
        Map result = new HashMap();
        if (name != null && parent != null) {
            String parentName = parent.getNodeName();
            if ("service".equalsIgnoreCase(name)) {
                result.put("type", new String[] { "compute", "data", "information", "application" });
                result.put("cost", new String[] { "1.0" });
                result.put("mappingID", new String[] { "" });
                result.put("mappingID", new String[] { "" });
            } else if ("compute".equalsIgnoreCase(name)) {
                result.put("middleware", new String[] { "globus", "unicore", "alchemi", "xgrid", "sge", "pbs", "fork", "condor" });
                result.put("firewall", new String[] { "false", "true" });
            } else if ("xgrid".equalsIgnoreCase(name)) {
                result.put("controller", new String[] { "default" });
                result.put("version", new String[] { "tp2" });
            } else if ("queue".equalsIgnoreCase(name)) {
                result.put("name", new String[] { "queue1" });
                result.put("cost", new String[] { "", "0" });
                result.put("priority", new String[] { "", "5" });
                result.put("limit", new String[] { "", "50" });
            } else if ("globus".equalsIgnoreCase(name)) {
                result.put("hostname", new String[] { "127.0.0.1" });
                result.put("jobmanager", new String[] { "", "jobmanager-fork", "jobmanager-sge", "jobmanager-pbs", "jobmanager-condor" });
                result.put("version", new String[] { "", "2.4", "3.2", "4.0" });
                result.put("service", new String[] { "" });
                result.put("fileStagingURL", new String[] { "http://localhost:10000//tmp" });
                result.put("configuration", new String[] { "" });
            } else if ("unicore".equalsIgnoreCase(name)) {
                result.put("gatewayURL", new String[] { "http://localhost" });
                result.put("version", new String[] { "4.1" });
            } else if ("alchemi".equalsIgnoreCase(name)) {
                result.put("managerURL", new String[] { "http://localhost" });
                result.put("version", new String[] { "1.0" });
            } else if ("pbs".equalsIgnoreCase(name)) {
                result.put("hostname", new String[] { "localhost" });
                result.put("version", new String[] { "2.3" });
            } else if ("condor".equalsIgnoreCase(name)) {
                result.put("hostname", new String[] { "localhost" });
                result.put("version", new String[] { "6.7" });
            } else if ("sge".equalsIgnoreCase(name)) {
                result.put("hostname", new String[] { "localhost" });
                result.put("version", new String[] {});
            } else if ("fork".equalsIgnoreCase(name)) {
                result.put("hostname", new String[] { "localhost" });
            } else if ("information".equalsIgnoreCase(name)) {
                result.put("type", new String[] { "replicaCatalog", "srbMCAT", "networkStatus" });
            } else if ("replicaCatalog".equalsIgnoreCase(name)) {
                result.put("replicaHost", new String[] { "http://localhost" });
                result.put("replicaTop", new String[] { "default" });
            } else if ("srbMCAT".equalsIgnoreCase(name)) {
                result.put("host", new String[] { "http://localhost" });
                result.put("port", new String[] { "6833" });
                result.put("domain", new String[] { "localhost" });
                result.put("home", new String[] { "home" });
                result.put("defaultResource", new String[] { "default" });
                result.put("authSchema", new String[] { "GSI_AUTH", "ENCRYPT1" });
                result.put("serverDN", new String[] {});
            } else if ("networkWeatherService".equalsIgnoreCase(name)) {
                result.put("nameServer", new String[] { "http://localhost" });
                result.put("port", new String[] { "10000" });
            } else if ("data".equalsIgnoreCase(name)) {
                result.put("type", new String[] { "gridftp", "srb" });
                result.put("hostname", new String[] { "http://localhost" });
                result.put("accessMode", new String[] { "read-only", "write-only", "read-wrtie" });
            } else if ("application".equalsIgnoreCase(name)) {
                result.put("url", new String[] { "http://localhost" });
            }
        }
        return result;
    }
