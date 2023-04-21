    public static void linkOnePermutationToJar(TreeLogger logger, ModuleDef module, ArtifactSet generatedArtifacts, PermutationResult permResult, File jarFile, PrecompileTaskOptions precompileOptions) throws UnableToCompleteException {
        try {
            if (jarFile.exists()) {
                boolean success = jarFile.delete();
                if (!success) {
                    logger.log(TreeLogger.ERROR, "Linker output file " + jarFile.getName() + " already exists and can't be deleted.");
                }
            }
            JarOutputStream jar = new JarOutputStream(new FileOutputStream(jarFile));
            StandardLinkerContext linkerContext = new StandardLinkerContext(logger, module, precompileOptions);
            StandardCompilationResult compilation = new StandardCompilationResult(permResult);
            addSelectionPermutations(compilation, permResult.getPermutation(), linkerContext);
            ArtifactSet permArtifacts = new ArtifactSet(generatedArtifacts);
            permArtifacts.addAll(permResult.getArtifacts());
            permArtifacts.add(compilation);
            ArtifactSet linkedArtifacts = linkerContext.invokeLinkForOnePermutation(logger, compilation, permArtifacts);
            for (EmittedArtifact art : linkedArtifacts.find(EmittedArtifact.class)) {
                Visibility visibility = art.getVisibility();
                String jarEntryPath = visibility.name() + "/";
                if (visibility == Visibility.Public) {
                    jarEntryPath += art.getPartialPath();
                } else {
                    jarEntryPath += prefixArtifactPath(art, linkerContext);
                }
                ZipEntry ze = new ZipEntry(jarEntryPath);
                ze.setTime(OutputFileSetOnJar.normalizeTimestamps ? 0 : art.getLastModified());
                jar.putNextEntry(ze);
                art.writeTo(logger, jar);
                jar.closeEntry();
            }
            int numSerializedArtifacts = 0;
            for (Artifact art : linkedArtifacts) {
                if (art.isTransferableFromShards() && !(art instanceof EmittedArtifact)) {
                    String jarEntryPath = "arts/" + numSerializedArtifacts++;
                    ZipEntry ze = new ZipEntry(jarEntryPath);
                    if (OutputFileSetOnJar.normalizeTimestamps) {
                        ze.setTime(0);
                    }
                    jar.putNextEntry(ze);
                    Util.writeObjectToStream(jar, art);
                    jar.closeEntry();
                }
            }
            jar.close();
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, "Error linking", e);
            throw new UnableToCompleteException();
        }
    }
