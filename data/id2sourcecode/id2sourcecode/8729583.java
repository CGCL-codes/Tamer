    public int cmdImport(Dictionary opts, Reader in, final PrintWriter out, Session session) {
        int retcode = 1;
        try {
            final String spec = (String) opts.get("url");
            final URL url = new URL(spec);
            if (url == null) {
                throw new Exception("URL Object construction failed");
            }
            AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    ConfigurationAdmin configAdmin = null;
                    PushbackReader reader = null;
                    try {
                        configAdmin = getCA();
                        CMDataReader cmDataReader = new CMDataReader();
                        reader = new PushbackReader(new BufferedReader(new InputStreamReader(url.openStream(), CMDataReader.ENCODING), 8192), 8);
                        Hashtable[] configs = cmDataReader.readCMDatas(reader);
                        for (int i = 0; i < configs.length; i++) {
                            String pid = (String) configs[i].get(CMDataReader.SERVICE_PID);
                            String fpid = (String) configs[i].get(CMDataReader.FACTORY_PID);
                            Configuration config;
                            if (fpid == null) {
                                config = configAdmin.getConfiguration(pid, null);
                            } else {
                                config = configAdmin.createFactoryConfiguration(fpid, null);
                            }
                            if (config.getBundleLocation() != null) {
                                config.setBundleLocation(null);
                            }
                            if (configs[i].get("service.bundleLocation") != null) {
                                configs[i].remove("service.bundleLocation");
                            }
                            config.update(configs[i]);
                        }
                    } finally {
                        if (reader != null) {
                            reader.close();
                        }
                        if (configAdmin != null) {
                            bc.ungetService(refCA);
                        }
                    }
                    return null;
                }
            });
            retcode = 0;
        } catch (MalformedURLException e) {
            out.println("Could not create URL. Details:");
            String reason = e.getMessage();
            out.println(reason == null ? "<unknown>" : reason);
        } catch (IOException e) {
            out.println("Import failed. Details:");
            String reason = e.getMessage();
            out.println(reason == null ? "<unknown>" : reason);
        } catch (PrivilegedActionException pae) {
            out.println("Import failed. Details:");
            String reason = pae.getException().toString();
            out.println(reason == null ? "<unknown>" : reason);
        } catch (Exception e) {
            out.println("Import failed. Details:");
            String reason = e.getMessage();
            out.println(reason == null ? "<unknown>" : reason);
        } finally {
        }
        return retcode;
    }
