    @SuppressWarnings("unchecked")
    public String load(String mainClass, String jarName) {
        if (record) {
            new File(recording).mkdirs();
        }
        try {
            if (jarName == null) {
                jarName = System.getProperty(JAVA_CLASS_PATH);
            }
            if (jarName.indexOf(":") != -1) {
                jarName = jarName.substring(0, jarName.indexOf(":"));
            }
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> e = jarFile.entries();
            Manifest manifest = jarFile.getManifest();
            String expandPaths[] = null;
            String expand = manifest.getMainAttributes().getValue(EXPAND);
            if (expand != null) {
                VERBOSE(EXPAND + "=" + expand);
                expandPaths = expand.split(",");
            }
            while (e.hasMoreElements()) {
                JarEntry entry = (JarEntry) e.nextElement();
                if (entry.isDirectory()) continue;
                boolean expanded = false;
                String name = entry.getName();
                if (expandPaths != null) {
                    for (int i = 0; i < expandPaths.length; i++) {
                        if (name.startsWith(expandPaths[i])) {
                            File dest = new File(name);
                            if (!dest.exists() || dest.lastModified() < entry.getTime()) {
                                INFO("Expanding " + name);
                                if (dest.exists()) INFO("Update because lastModified=" + new Date(dest.lastModified()) + ", entry=" + new Date(entry.getTime()));
                                if (dest.getParentFile() != null) {
                                    dest.getParentFile().mkdirs();
                                }
                                VERBOSE("using jarFile.getInputStream(" + entry + ")");
                                InputStream is = jarFile.getInputStream(entry);
                                FileOutputStream os = new FileOutputStream(dest);
                                copy(is, os);
                                is.close();
                                os.close();
                            } else {
                                VERBOSE(name + " already expanded");
                            }
                            expanded = true;
                            break;
                        }
                    }
                }
                if (expanded) continue;
                String jar = entry.getName();
                if (wrapDir != null && jar.startsWith(wrapDir) || jar.startsWith(LIB_PREFIX) || jar.startsWith(MAIN_PREFIX)) {
                    if (wrapDir != null && !entry.getName().startsWith(wrapDir)) continue;
                    INFO("caching " + jar);
                    VERBOSE("using jarFile.getInputStream(" + entry + ")");
                    {
                        InputStream is = jarFile.getInputStream(entry);
                        if (is == null) throw new IOException("Unable to load resource /" + jar + " using " + this);
                        loadByteCode(is, jar);
                    }
                    if (jar.startsWith(MAIN_PREFIX)) {
                        if (mainClass == null) {
                            JarInputStream jis = new JarInputStream(jarFile.getInputStream(entry));
                            mainClass = jis.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                            mainJar = jar;
                        } else if (mainJar != null) {
                            WARNING("A main class is defined in multiple jar files inside " + MAIN_PREFIX + mainJar + " and " + jar);
                            WARNING("The main class " + mainClass + " from " + mainJar + " will be used");
                        }
                    }
                } else if (wrapDir == null && name.startsWith(UNPACK)) {
                    InputStream is = this.getClass().getResourceAsStream("/" + jar);
                    if (is == null) throw new IOException(jar);
                    File dir = new File(TMP);
                    File sentinel = new File(dir, jar.replace('/', '.'));
                    if (!sentinel.exists()) {
                        INFO("unpacking " + jar + " into " + dir.getCanonicalPath());
                        loadByteCode(is, jar, TMP);
                        sentinel.getParentFile().mkdirs();
                        sentinel.createNewFile();
                    }
                } else if (name.endsWith(CLASS)) {
                    loadBytes(entry, jarFile.getInputStream(entry), "/", null);
                }
            }
            if (new File(Boot.MAIN_JAR_EXT).exists()) {
                InputStream mainIs = new FileInputStream(Boot.MAIN_JAR_EXT + Boot.MAIN_JAR);
                if (mainIs != null) {
                    loadByteCode(mainIs, Boot.MAIN_JAR_EXT + Boot.MAIN_JAR);
                }
            }
            this.classdir = manifest.getMainAttributes().getValue(CLASSDIR);
            if (this.classdir != null) {
                File clDir = new File(this.classdir);
                if (clDir.exists()) {
                    for (String file : clDir.list()) {
                        byteCode.put(this.classdir + "/" + file, new ByteCode(null, null, null, null));
                    }
                }
            }
            if (mainClass == null) {
                mainClass = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
            }
            String env = manifest.getMainAttributes().getValue(ENV);
            String envs[] = new String[0];
            if (env != null) {
                VERBOSE(ENV + "=" + env);
                envs = env.split(",");
            }
            for (int i = 0; i < envs.length; i++) {
                String[] element = envs[i].split("=");
                System.setProperty(element[0], element[1]);
            }
        } catch (IOException iox) {
            System.err.println("Unable to load resource: " + iox);
            iox.printStackTrace(System.err);
        }
        return mainClass;
    }
