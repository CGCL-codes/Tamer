    private void createJavaStub() {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(output));
            ZipEntry ze = new ZipEntry(stub.class.getName().replace('.', '/') + "16x16.gif");
            zos.putNextEntry(ze);
            copy(iconFile != null ? new FileInputStream(iconFile) : stub.class.getResourceAsStream("stub16x16.gif"), zos);
            ze = new ZipEntry(stub.class.getName().replace('.', '/') + ".class");
            zos.putNextEntry(ze);
            copy(stub.class.getResourceAsStream("stub.class"), zos);
            ze = new ZipEntry(stub.class.getName().replace('.', '/') + ".properties");
            zos.putNextEntry(ze);
            Properties stubProperties = new Properties();
            stubProperties.put(__MODE_, isConsole ? "console" : "gui");
            stubProperties.put(__EXECUTABLE__NAME__, execute);
            stubProperties.put(__EXECUTABLE__PATH__, workingDirectory);
            stubProperties.store(zos, "ORANGEVOLT stub 1.0 properties");
            zos.close();
        } catch (Exception e) {
            throw new BuildException("Unable to copy jar stub", e);
        }
        org.apache.tools.ant.taskdefs.Jar jar = new org.apache.tools.ant.taskdefs.Jar();
        jar.setCompress(true);
        jar.setUpdate(true);
        ZipFileSet zfs = new ZipFileSet();
        zfs.setSrc(archive);
        jar.addZipfileset(zfs);
        jar.setDestFile(output);
        try {
            Manifest manifest = Manifest.getDefaultManifest();
            Manifest.Section section = manifest.getMainSection();
            section.addConfiguredAttribute(new Manifest.Attribute("Main-Class", "net.sf.ovanttasks.ovanttasks.stub"));
            jar.addConfiguredManifest(manifest);
            jar.setOwningTarget(getOwningTarget());
            jar.setProject(getProject());
            jar.setTaskName(getTaskName());
            jar.execute();
        } catch (ManifestException ex) {
            throw new BuildException("ManifestException occured.", ex);
        }
    }
