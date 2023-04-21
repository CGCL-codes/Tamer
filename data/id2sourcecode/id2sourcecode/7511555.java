    protected void addOneJarBoot(ZipOutputStream zOut) throws IOException {
        if (zOut == null) return;
        if (onejar != null) {
            includeZip(onejar, zOut);
        } else {
            String ONE_JAR_BOOT = "one-jar-boot.jar";
            InputStream is = OneJarTask.class.getResourceAsStream(ONE_JAR_BOOT);
            if (is == null) throw new IOException("Unable to load default " + ONE_JAR_BOOT + ": consider using the <one-jar onejarboot=\"...\"> option.");
            JarInputStream jis = new JarInputStream(is);
            Manifest manifest = new Manifest();
            java.util.jar.Manifest jmanifest = jis.getManifest();
            java.util.jar.Attributes jattributes = jmanifest.getMainAttributes();
            try {
                manifest.addConfiguredAttribute(new Attribute("Created-By", "One-Jar 0.98 Ant taskdef"));
                manifest.addConfiguredAttribute(new Attribute(MAIN_CLASS, jattributes.getValue(MAIN_CLASS)));
                if (oneJarMainClass != null) {
                    manifest.addConfiguredAttribute(new Attribute(Boot.ONE_JAR_MAIN_CLASS, oneJarMainClass));
                }
                super.addConfiguredManifest(manifest);
            } catch (ManifestException mx) {
                throw new BuildException(mx);
            }
            super.initZipOutputStream(zOut);
            ZipEntry entry = jis.getNextEntry();
            while (entry != null) {
                if (entry.getName().endsWith(CLASS) || entry.getName().equals(".version") || entry.getName().endsWith("license.txt")) {
                    log("entry=" + entry.getName(), Project.MSG_DEBUG);
                    zOut.putNextEntry(new org.apache.tools.zip.ZipEntry(entry));
                    copy(jis, zOut, false);
                }
                entry = jis.getNextJarEntry();
            }
        }
    }
