    private void parseRepositoryFile(int hopCount, String urlStr) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            String auth = m_context.getProperty("http.proxyAuth");
            if ((auth != null) && (auth.length() > 0)) {
                if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
                    String base64 = Util.base64Encode(auth);
                    conn.setRequestProperty("Proxy-Authorization", "Basic " + base64);
                }
            }
            String basicAuth = m_context.getProperty("http.basicAuth");
            if (basicAuth != null && !"".equals(basicAuth)) {
                if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
                    String base64 = Util.base64Encode(basicAuth);
                    conn.setRequestProperty("Authorization", "Basic " + base64);
                }
            }
            conn.setRequestProperty("User-Agent", getUserAgentForBundle(m_context.getBundle()) + " " + getUserAgentForBundle(m_context.getBundle(0)));
            is = conn.getInputStream();
            XmlCommonHandler handler = new XmlCommonHandler();
            handler.addType("bundles", ArrayList.class);
            handler.addType("repository", HashMap.class);
            handler.addType("extern-repositories", ArrayList.class);
            handler.addType("bundle", MultivalueMap.class);
            handler.addType("import-package", HashMap.class);
            handler.addType("export-package", HashMap.class);
            handler.setDefaultType(String.class);
            br = new BufferedReader(new InputStreamReader(is));
            KXmlSAXParser parser;
            parser = new KXmlSAXParser(br);
            try {
                parser.parseXML(handler);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            List root = (List) handler.getRoot();
            for (int bundleIdx = 0; bundleIdx < root.size(); bundleIdx++) {
                Object obj = root.get(bundleIdx);
                if (obj instanceof HashMap) {
                    Map repoMap = new TreeMap(new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareToIgnoreCase(o2.toString());
                        }
                    });
                    repoMap.putAll((Map) obj);
                    if (hopCount > 0) {
                        List externList = (List) repoMap.get(EXTERN_REPOSITORY_TAG);
                        for (int i = 0; (externList != null) && (i < externList.size()); i++) {
                            parseRepositoryFile(hopCount - 1, (String) externList.get(i));
                        }
                    }
                } else if (obj instanceof MultivalueMap) {
                    Map bundleMap = new TreeMap(new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareToIgnoreCase(o2.toString());
                        }
                    });
                    bundleMap.putAll((Map) obj);
                    Object target = bundleMap.get(BundleRecord.IMPORT_PACKAGE);
                    if (target != null) {
                        bundleMap.put(BundleRecord.IMPORT_PACKAGE, convertPackageDeclarations(target));
                    }
                    target = bundleMap.get(BundleRecord.EXPORT_PACKAGE);
                    if (target != null) {
                        bundleMap.put(BundleRecord.EXPORT_PACKAGE, convertPackageDeclarations(target));
                    }
                    BundleRecord record = new BundleRecord(bundleMap);
                    try {
                        PackageDeclaration[] exportPkgs = (PackageDeclaration[]) record.getAttribute(BundleRecord.EXPORT_PACKAGE);
                        for (int exportIdx = 0; (exportPkgs != null) && (exportIdx < exportPkgs.length); exportIdx++) {
                            ArrayList exporterList = (ArrayList) m_exportPackageMap.get(exportPkgs[exportIdx].getName());
                            if (exporterList == null) {
                                exporterList = new ArrayList();
                            }
                            Object[] exportInfo = new Object[2];
                            exportInfo[EXPORT_PACKAGE_IDX] = exportPkgs[exportIdx];
                            exportInfo[EXPORT_BUNDLE_IDX] = record;
                            exporterList.add(exportInfo);
                            m_exportPackageMap.put(exportPkgs[exportIdx].getName(), exporterList);
                        }
                        m_bundleList.add(record);
                    } catch (IllegalArgumentException ex) {
                    }
                }
            }
        } catch (MalformedURLException ex) {
            System.err.println("Error: " + ex);
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ex) {
            }
        }
        Collections.sort(m_bundleList, new Comparator() {

            public int compare(Object o1, Object o2) {
                BundleRecord r1 = (BundleRecord) o1;
                BundleRecord r2 = (BundleRecord) o2;
                String name1 = (String) r1.getAttribute(BundleRecord.BUNDLE_NAME);
                String name2 = (String) r2.getAttribute(BundleRecord.BUNDLE_NAME);
                return name1.compareToIgnoreCase(name2);
            }
        });
    }
