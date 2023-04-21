    private XMLMemento loadBundlesFile() throws CoreException {
        XMLMemento bundles = null;
        FileReader file = null;
        IPath bundleFile = null;
        try {
            bundleFile = new Path("$nl$/bundles.xml");
            URL url = FileLocator.find(ignisProjectsPlugin.getDefault().getBundle(), bundleFile, null);
            Reader br = new BufferedReader(new InputStreamReader(url.openStream()));
            bundles = XMLMemento.createReadRoot(br);
        } catch (FileNotFoundException e) {
            ignisProjectUtils.throwCoreException("Bundle file " + bundleFile.toOSString() + " not found.", e);
        } catch (NullPointerException e) {
            ignisProjectUtils.throwCoreException("Bundle file " + bundleFile.toOSString() + " not found.", e);
        } catch (Exception e) {
            ignisProjectUtils.throwCoreException("Fail to handle Bundle file", e);
        } finally {
            try {
                if (file != null) file.close();
            } catch (IOException e) {
                ignisProjectsPlugin.log("Fail to close Bundle file");
            }
        }
        return bundles;
    }
