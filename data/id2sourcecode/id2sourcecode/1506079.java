    private static void initSevenZipFromPlatformJARIntern(String platform, File tmpDirectory) throws SevenZipNativeInitializationException {
        try {
            autoInitializationWillOccur = false;
            if (initializationSuccessful) {
                return;
            }
            String pathInJAR = platform;
            if (pathInJAR == null) {
                pathInJAR = getPlatformBestMatch();
            }
            usedPlatform = pathInJAR;
            pathInJAR = "/" + pathInJAR + "/";
            InputStream sevenZipJBindingLibProperties = SevenZip.class.getResourceAsStream(pathInJAR + SEVENZIPJBINDING_LIB_PROPERTIES_FILENAME);
            if (sevenZipJBindingLibProperties == null) {
                throwInitException("error loading property file '" + pathInJAR + SEVENZIPJBINDING_LIB_PROPERTIES_FILENAME + "' from a jar-file 'sevenzipjbinding-<Platform>.jar'. Is the platform jar-file not in the class path?");
            }
            Properties properties = new Properties();
            try {
                properties.load(sevenZipJBindingLibProperties);
            } catch (IOException e) {
                throwInitException("error loading property file '" + SEVENZIPJBINDING_LIB_PROPERTIES_FILENAME + "' from a jar-file 'sevenzipjbinding-<Platform>.jar'");
            }
            File tmpDirFile;
            if (tmpDirectory != null) {
                tmpDirFile = tmpDirectory;
            } else {
                String systemPropertyTmp = System.getProperty(SYSTEM_PROPERTY_TMP);
                if (systemPropertyTmp == null) {
                    throwInitException("can't determinte tmp directory. Use may use -D" + SYSTEM_PROPERTY_TMP + "=<path to tmp dir> parameter for jvm to fix this.");
                }
                tmpDirFile = new File(systemPropertyTmp);
            }
            if (!tmpDirFile.exists() || !tmpDirFile.isDirectory()) {
                throwInitException("invalid tmp directory '" + tmpDirectory + "'");
            }
            if (!tmpDirFile.canWrite()) {
                throwInitException("can't create files in '" + tmpDirFile.getAbsolutePath() + "'");
            }
            File tmpSubdirFile = new File(tmpDirFile.getAbsolutePath() + File.separator + "SevenZipJBinding-" + new Random().nextInt(10000000));
            if (!tmpSubdirFile.mkdir()) {
                throwInitException("Directory '" + tmpDirFile.getAbsolutePath() + "' couldn't be created");
            }
            tmpSubdirFile.deleteOnExit();
            List<File> librariesToInit = new ArrayList<File>(2);
            for (int i = 1; ; i++) {
                String propertyName = String.format(PROPERTY_SEVENZIPJBINDING_LIBNAME, Integer.valueOf(i));
                String libname = properties.getProperty(propertyName);
                if (libname == null) {
                    if (librariesToInit.size() == 0) {
                        throwInitException("property file '" + SEVENZIPJBINDING_LIB_PROPERTIES_FILENAME + "' from a jar-file 'sevenzipjbinding-<Platform>.jar' don't contain the property named '" + propertyName + "'");
                    } else {
                        break;
                    }
                }
                File libTmpFile = new File(tmpSubdirFile.getAbsolutePath() + File.separatorChar + libname);
                libTmpFile.deleteOnExit();
                InputStream libInputStream = SevenZip.class.getResourceAsStream(pathInJAR + libname);
                if (libInputStream == null) {
                    throwInitException("error loading native library '" + libname + "' from a jar-file 'sevenzipjbinding-<Platform>.jar'.");
                }
                copyLibraryToFS(libTmpFile, libInputStream);
                librariesToInit.add(libTmpFile);
            }
            for (int i = librariesToInit.size() - 1; i != -1; i--) {
                System.load(librariesToInit.get(i).getAbsolutePath());
            }
            nativeInitialization();
        } catch (SevenZipNativeInitializationException sevenZipNativeInitializationException) {
            lastInitializationException = sevenZipNativeInitializationException;
            throw sevenZipNativeInitializationException;
        }
    }
