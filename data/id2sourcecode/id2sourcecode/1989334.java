    public static String getDataSourceName(String cname, boolean readOnly) {
        String postfix = readOnly ? ".read" : ".write";
        String nom = null;
        while (!dataSources.containsKey(cname + postfix) && !dataSources.containsKey(cname)) {
            int idx = cname.lastIndexOf(".");
            if (idx != -1) {
                cname = cname.substring(0, idx);
            } else {
                break;
            }
        }
        if (dataSources.containsKey(cname + postfix)) {
            nom = dataSources.get(cname + postfix);
        } else if (dataSources.containsKey(cname)) {
            nom = dataSources.get(cname);
        }
        return nom;
    }
