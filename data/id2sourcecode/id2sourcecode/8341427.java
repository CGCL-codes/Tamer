    public static void buildOperations(String path, String mibsDir, String className, String baseOidInclude, String baseOidExclude, boolean extendedDocumentation) throws Exception {
        SnmpOperationsBuilder.extendedDocumentation = extendedDocumentation;
        if (!className.endsWith(".java")) {
            className += ".java";
        }
        MibLoader loader = new MibLoader();
        File f = new File(mibsDir);
        File[] mibFiles = f.listFiles();
        loader.addDir(f);
        for (int i = 0; i < mibFiles.length; i++) {
            if (mibFiles[i].getName().indexOf(".mib") != -1) {
                try {
                    loader.load(mibFiles[i]);
                } catch (Exception e) {
                    throw new Exception("Fail loading " + mibFiles[i] + " mib: " + e.getMessage());
                }
            }
        }
        Mib[] mibs = loader.getAllMibs();
        FileWriter file = new FileWriter(path + "/" + className);
        file.append("import systemobject.snmp.Snmp;" + ENTER);
        file.append("import org.opennms.protocols.snmp.SnmpSyntax;" + ENTER);
        file.append("import org.opennms.protocols.snmp.SnmpVarBind;" + ENTER);
        file.append(ENTER);
        file.append("public class MibsOperations { " + ENTER);
        file.append(ENTER);
        file.append(tab(1) + "Snmp snmp;" + ENTER);
        file.append(ENTER);
        file.append(tab(1) + "public MibsOperations(Snmp snmp) { " + ENTER);
        file.append(tab(2) + "this.snmp = snmp;" + ENTER);
        file.append(tab(1) + "}" + ENTER);
        file.append(ENTER);
        file.flush();
        file.append(ENTER + tab(1) + "/**" + ENTER);
        file.append(tab(1) + " * This Method Should Be Used For Building The MIB OID Using The Given Arguments: " + ENTER);
        file.append(tab(1) + " * If The Given Arguments Are \"null\" Or With length=\"0\", It Will Add \"0\" To The End Of The OID." + ENTER);
        file.append(tab(1) + " * " + ENTER);
        file.append(tab(1) + " * @param mibName MIB symbol name as String" + ENTER);
        file.append(tab(1) + " * @param args MIB entry extensions as String[] (instance id ETC), will be seperated with \".\"" + ENTER);
        file.append(tab(1) + " * " + ENTER);
        file.append(tab(1) + " * @return MIB entry full OID (\"X.X.X.X.X.X....X.X\" format)" + ENTER);
        file.append(tab(1) + " * " + ENTER);
        file.append(tab(1) + " * @throws Exception if the given MIB name wasn't found in the MIBs map" + ENTER);
        file.append(tab(1) + " */" + ENTER);
        file.append(tab(1) + "protected String buildOid(String mibName, String[] args) throws Exception {" + ENTER);
        file.append(tab(2) + "String oid = snmp.getOidFromMap( mibName );" + ENTER);
        file.append(tab(2) + "if ( args == null || args.length == 0 ) { " + ENTER);
        file.append(tab(3) + "oid += \".0\";" + ENTER);
        file.append(tab(2) + "} else {" + ENTER);
        file.append(tab(3) + "for ( int i=0; i < args.length; i++ ) {" + ENTER);
        file.append(tab(4) + "oid += \".\"+args[i];" + ENTER);
        file.append(tab(3) + "}" + ENTER);
        file.append(tab(2) + "}" + ENTER);
        file.append(tab(2) + "return oid;" + ENTER);
        file.append(tab(1) + "}" + ENTER);
        file.flush();
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, String> commentsMap = new HashMap<String, String>();
        String mibName, oid, comments;
        String[] result;
        boolean set;
        boolean access;
        for (int j = 0; j < mibs.length; j++) {
            Object[] arr = mibs[j].getAllSymbols().toArray();
            MibSymbol symbol;
            MibValue value = null;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != null) {
                    symbol = (MibSymbol) arr[i];
                    if (symbol instanceof MibValueSymbol) {
                        value = ((MibValueSymbol) symbol).getValue();
                        if (value != null) {
                            mibName = value.getName();
                            if (mibName != null) {
                                if (map.get(mibName.trim()) == null) {
                                    oid = value.toString();
                                    if (oid != null && (baseOidInclude == null || oid.contains(baseOidInclude))) {
                                        if (baseOidExclude == null || !oid.contains(baseOidExclude)) {
                                            map.put(oid.trim(), mibName.trim());
                                            commentsMap.put(oid.trim(), ((MibValueSymbol) symbol).getType().toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Object[] arr = map.keySet().toArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                set = false;
                access = false;
                oid = (String) arr[i];
                mibName = (String) map.get(oid);
                comments = (String) commentsMap.get(oid);
                result = split(comments);
                int k = 0;
                for (; k < result.length; k++) {
                    if (result[k].trim().startsWith("object-type (")) {
                        for (; k < result.length; k++) {
                            if (result[k].trim().startsWith("access: read-write") || result[k].trim().startsWith("access: read-create")) {
                                access = true;
                                set = true;
                                k = result.length;
                            } else if (result[k].trim().startsWith("access: read-only")) {
                                access = true;
                                set = false;
                                k = result.length;
                            } else if (result[k].trim().startsWith("access: not-accessible")) {
                                access = false;
                                set = false;
                                k = result.length;
                            }
                        }
                    }
                }
                if (access) {
                    if (set) {
                        file.append(documentation(true, mibName, oid, comments));
                        file.append(tab(1) + "protected void set" + mibName.substring(0, 1).toUpperCase() + mibName.substring(1) + " (String[] args, SnmpSyntax value) throws Exception {" + ENTER);
                        file.append(tab(2) + "snmp.set( new SnmpVarBind[] { new SnmpVarBind( buildOid( \"" + mibName + "\", args ), value) }, true );" + ENTER);
                        file.append(tab(1) + "}" + ENTER);
                        file.flush();
                    }
                    file.append(documentation(false, mibName, oid, comments));
                    file.append(tab(1) + "protected void get" + mibName.substring(0, 1).toUpperCase() + mibName.substring(1) + " (String[] args) throws Exception { " + ENTER);
                    file.append(tab(2) + "snmp.get( buildOid( \"" + mibName + "\", args ) );" + ENTER);
                    file.append(tab(1) + " }" + ENTER);
                    file.flush();
                }
            }
        }
        file.append("} " + ENTER);
        file.flush();
        file.close();
    }
