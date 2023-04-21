    protected void internalExecute() throws MojoExecutionException, MojoFailureException, IOException {
        if (dependenciesSkip) {
            getLog().info("Dependencies extraction skipped");
            return;
        }
        File baseDir = project.getBasedir();
        try {
            if (dependenciesClean) {
                if (!dependenciesSkipJars) {
                    FileUtils.deleteDirectory(new File(baseDir, "lib"));
                }
                FileUtils.deleteDirectory(new File(baseDir, "modules"));
            }
            Set<?> projectArtifacts = project.getArtifacts();
            Set<Artifact> excludedArtifacts = new HashSet<Artifact>();
            Artifact playSeleniumJunit4Artifact = getDependencyArtifact(projectArtifacts, "com.google.code.maven-play-plugin", "play-selenium-junit4", "jar");
            if (playSeleniumJunit4Artifact != null) {
                excludedArtifacts.addAll(getDependencyArtifacts(projectArtifacts, playSeleniumJunit4Artifact));
            }
            Set<Artifact> filteredArtifacts = new HashSet<Artifact>();
            for (Iterator<?> iter = projectArtifacts.iterator(); iter.hasNext(); ) {
                Artifact artifact = (Artifact) iter.next();
                if (artifact.getArtifactHandler().isAddedToClasspath() && !Artifact.SCOPE_PROVIDED.equals(artifact.getScope()) && !excludedArtifacts.contains(artifact)) {
                    filteredArtifacts.add(artifact);
                }
            }
            Map<String, Artifact> moduleArtifacts = findAllModuleArtifacts(true);
            for (Map.Entry<String, Artifact> moduleArtifactEntry : moduleArtifacts.entrySet()) {
                String moduleName = moduleArtifactEntry.getKey();
                Artifact moduleZipArtifact = moduleArtifactEntry.getValue();
                if (!Artifact.SCOPE_PROVIDED.equals(moduleZipArtifact.getScope())) {
                    checkPotentialReactorProblem(moduleZipArtifact);
                    File moduleZipFile = moduleZipArtifact.getFile();
                    String moduleSubDir = String.format("modules/%s-%s/", moduleName, moduleZipArtifact.getVersion());
                    File moduleDirectory = new File(baseDir, moduleSubDir);
                    createModuleDirectory(moduleDirectory, dependenciesOverwrite || moduleDirectory.lastModified() < moduleZipFile.lastModified());
                    if (moduleDirectory.list().length == 0) {
                        UnArchiver zipUnArchiver = archiverManager.getUnArchiver("zip");
                        zipUnArchiver.setSourceFile(moduleZipFile);
                        zipUnArchiver.setDestDirectory(moduleDirectory);
                        zipUnArchiver.setOverwrite(false);
                        zipUnArchiver.extract();
                        moduleDirectory.setLastModified(System.currentTimeMillis());
                        if ("scala".equals(moduleName)) {
                            scalaHack(moduleDirectory, filteredArtifacts);
                        }
                        if (!dependenciesSkipJars) {
                            Set<Artifact> dependencySubtree = getModuleDependencyArtifacts(filteredArtifacts, moduleZipArtifact);
                            if (!dependencySubtree.isEmpty()) {
                                File moduleLibDir = new File(moduleDirectory, "lib");
                                createLibDirectory(moduleLibDir);
                                for (Artifact classPathArtifact : dependencySubtree) {
                                    File jarFile = classPathArtifact.getFile();
                                    if (dependenciesOverwrite) {
                                        FileUtils.copyFileToDirectory(jarFile, moduleLibDir);
                                    } else {
                                        if (jarFile == null) {
                                            getLog().info("null file");
                                        }
                                        FileUtils.copyFileToDirectoryIfModified(jarFile, moduleLibDir);
                                    }
                                    filteredArtifacts.remove(classPathArtifact);
                                }
                            }
                        }
                    }
                }
            }
            if (!dependenciesSkipJars && !filteredArtifacts.isEmpty()) {
                File libDir = new File(baseDir, "lib");
                createLibDirectory(libDir);
                for (Iterator<?> iter = filteredArtifacts.iterator(); iter.hasNext(); ) {
                    Artifact classPathArtifact = (Artifact) iter.next();
                    File jarFile = classPathArtifact.getFile();
                    if (dependenciesOverwrite) {
                        FileUtils.copyFileToDirectory(jarFile, libDir);
                    } else {
                        FileUtils.copyFileToDirectoryIfModified(jarFile, libDir);
                    }
                }
            }
        } catch (ArchiverException e) {
            throw new MojoExecutionException("?", e);
        } catch (DependencyTreeBuilderException e) {
            throw new MojoExecutionException("?", e);
        } catch (NoSuchArchiverException e) {
            throw new MojoExecutionException("?", e);
        }
    }
