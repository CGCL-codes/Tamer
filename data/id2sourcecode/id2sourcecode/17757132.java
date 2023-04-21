    public void execute() throws BuildException {
        if (mRootDir == null) {
            throw new BuildException("Required attribute \"dir\" is not set.");
        }
        if (mJarAttrs.isEmpty() && mJarFileSets.isEmpty() && mJarFileLists.isEmpty()) {
            throw new BuildException("Either the attribute \"jars\" must " + "be set, or one or more jarfilelists or " + "jarfilesets must be added.");
        }
        if (!mJarAttrs.isEmpty() && (!mJarFileSets.isEmpty() || !mJarFileLists.isEmpty())) {
            throw new BuildException("Cannot set both the attribute " + "\"jars\" and use jar " + "filesets/filelists.  Use " + "only one or the other.");
        }
        for (Iterator i = mProps.getDocumentTypes().iterator(); i.hasNext(); ) {
            AppBundleProperties.DocumentType documentType = (AppBundleProperties.DocumentType) i.next();
            if (documentType.role == null || documentType.name == null) {
                throw new BuildException("Document types must specify both a name and a role.");
            }
            if (documentType.osTypes == null && documentType.extensions == null) {
                throw new BuildException("Document types must specify OS types and/or extensions.");
            }
        }
        if (mProps.getCFBundleName() == null) {
            throw new BuildException("Required attribute \"name\" is not set.");
        }
        if (mProps.getMainClass() == null) {
            throw new BuildException("Required attribute \"mainclass\" is " + "not set.");
        }
        if (mAboutMenuName == null) {
            mAboutMenuName = mProps.getCFBundleName();
        }
        mProps.addJavaProperty("com.apple.mrj.application.apple.menu.about.name", mAboutMenuName);
        mProps.addJavaProperty("com.apple.smallTabs", Boolean.toString(mSmallTabs));
        String antiAliasedProperty = useOldPropertyNames() ? "com.apple.macosx.AntiAliasedGraphicsOn" : "apple.awt.antialiasing";
        mProps.addJavaProperty(antiAliasedProperty, Boolean.toString(mAntiAliasedGraphics));
        String antiAliasedTextProperty = useOldPropertyNames() ? "com.apple.macosx.AntiAliasedTextOn" : "apple.awt.textantialiasing";
        mProps.addJavaProperty(antiAliasedTextProperty, Boolean.toString(mAntiAliasedText));
        mProps.addJavaProperty("com.apple.mrj.application.live-resize", Boolean.toString(mLiveResize));
        String screenMenuBarProperty = useOldPropertyNames() ? "com.apple.macos.useScreenMenuBar" : "apple.laf.useScreenMenuBar";
        mProps.addJavaProperty(screenMenuBarProperty, Boolean.toString(mScreenMenuBar));
        if (!useOldPropertyNames()) {
            mProps.addJavaProperty("apple.awt.showGrowBox", Boolean.toString(mGrowbox));
        }
        if (useOldPropertyNames()) {
            mProps.addJavaProperty("com.apple.mrj.application.growbox.intrudes", Boolean.toString(mGrowboxIntrudes));
        }
        if (!mRootDir.exists() || (mRootDir.exists() && !mRootDir.isDirectory())) {
            throw new BuildException("Destination directory specified by \"dir\" " + "attribute must already exist.");
        }
        File bundleDir = new File(mRootDir, mProps.getCFBundleName() + ".app");
        if (bundleDir.exists()) {
            throw new BuildException("The App Bundle " + bundleDir.getName() + " already exists, cannot continue.");
        }
        System.out.println("Creating application bundle " + bundleDir);
        if (!bundleDir.mkdir()) {
            throw new BuildException("Unable to create bundle: " + bundleDir);
        }
        mContentsDir = new File(bundleDir, "Contents");
        if (!mContentsDir.mkdir()) {
            throw new BuildException("Unable to create directory " + mContentsDir);
        }
        mMacOsDir = new File(mContentsDir, "MacOS");
        if (!mMacOsDir.mkdir()) {
            throw new BuildException("Unable to create directory " + mMacOsDir);
        }
        mResourceDir = new File(mContentsDir, "Resources");
        if (!mResourceDir.mkdir()) {
            throw new BuildException("Unable to create directory " + mResourceDir);
        }
        mJavaDir = new File(mResourceDir, "Java");
        if (!mJavaDir.mkdir()) {
            throw new BuildException("Unable to create directory " + mJavaDir);
        }
        try {
            if (mAppIcon != null) {
                mFileUtils.copyFile(mAppIcon, new File(mResourceDir, mAppIcon.getName()));
            }
            for (Iterator i = mProps.getDocumentTypes().iterator(); i.hasNext(); ) {
                AppBundleProperties.DocumentType documentType = (AppBundleProperties.DocumentType) i.next();
                if (documentType.iconFile != null) {
                    mFileUtils.copyFile(documentType.iconFile, new File(mResourceDir, documentType.iconFile.getName()));
                }
            }
        } catch (IOException ex) {
            throw new BuildException("Cannot copy icon file: " + ex);
        }
        processJarAttrs();
        processJarFileSets();
        processJarFileLists();
        processExecAttrs();
        processExecFileSets();
        processExecFileLists();
        if (mVerbose) {
            System.out.println("Setting CLASSPATH: " + mClassPath.toString());
        }
        mProps.setClassPath(mClassPath.toString());
        copyApplicationStub();
        writeInfoPlist();
        writePkgInfo();
    }
