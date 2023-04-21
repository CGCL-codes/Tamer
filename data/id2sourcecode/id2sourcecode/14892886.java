    private void processUnextractedModule(File jarFile) throws MojoExecutionException {
        JarFile jar;
        try {
            jar = new JarFile(jarFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Error while reading JAR file " + jarFile, e);
        }
        ZipEntry descEntry = jar.getEntry("META-INF/pustefix-module.xml");
        if (descEntry != null) {
            String moduleName;
            try {
                InputStream descIn = jar.getInputStream(descEntry);
                moduleName = getModuleName(descIn);
                descIn.close();
            } catch (IOException x) {
                throw new MojoExecutionException("Can't read module name from descriptor: " + jarFile.getAbsolutePath());
            }
            String[] modIncludes = DEFAULT_INCLUDES;
            if (includes != null) modIncludes = includes;
            File modulesSrcDir = new File(modulesdir);
            File destDir = new File(modulesDestDirname);
            for (String modInclude : modIncludes) {
                ZipEntry entry = jar.getEntry("PUSTEFIX-INF/" + modInclude);
                if (entry != null) {
                    File extracted = new File(modulesSrcDir, moduleName + "/" + modInclude);
                    String moduleURI = "module://" + moduleName + "/PUSTEFIX-INF/" + modInclude;
                    if (!extracted.exists()) {
                        File destFile = new File(destDir, moduleName + "/" + modInclude);
                        if (mergeSuffix != null) {
                            String name = destFile.getName();
                            int ind = name.indexOf('.');
                            if (ind > -1) {
                                name = name.substring(0, ind) + mergeSuffix + name.substring(ind);
                            } else {
                                throw new MojoExecutionException("Expected file name containing file extension: " + destFile.getAbsolutePath());
                            }
                            destFile = new File(destFile.getParentFile(), name);
                        }
                        if (!destFile.exists()) {
                            try {
                                if (!destFile.getParentFile().exists()) {
                                    destFile.getParentFile().mkdirs();
                                }
                                InputStream in = jar.getInputStream(entry);
                                FileOutputStream out = new FileOutputStream(destFile);
                                byte[] buffer = new byte[4096];
                                int no = 0;
                                try {
                                    while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
                                } finally {
                                    in.close();
                                    out.close();
                                }
                            } catch (IOException x) {
                                throw new MojoExecutionException("Error copying statusmessages from '" + moduleURI + "' to '" + destFile.getAbsolutePath() + "'.", x);
                            }
                            try {
                                Document doc = Xml.parseMutable(destFile);
                                addComment(doc, moduleURI);
                                Xml.serialize(doc, destFile, true, true);
                            } catch (Exception x) {
                                throw new MojoExecutionException("Error adding comment to statusmessages file '" + destFile.getAbsolutePath() + "'.", x);
                            }
                            getLog().info("Created " + destFile + " from source file " + moduleURI);
                        } else {
                            try {
                                InputStream in = jar.getInputStream(entry);
                                InputSource src = new InputSource();
                                src.setSystemId(moduleURI);
                                src.setByteStream(in);
                                Merge merge = new Merge(src, selection, destFile, false);
                                merge.run();
                                in.close();
                            } catch (Exception x) {
                                throw new MojoExecutionException("Merging to file " + destFile.getAbsolutePath() + " failed.", x);
                            }
                            getLog().info("Merged source file " + moduleURI + " into " + destFile);
                        }
                    }
                }
            }
        }
    }
