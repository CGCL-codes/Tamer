    public void processZip(File zipFile) throws ZipException, IOException {
        if (verbose) System.out.println("Processing " + zipFile.getPath());
        ZipFile zip = new ZipFile(zipFile);
        initialize(zip);
        String extension = unpacking ? "pack.gz" : ".jar";
        File tempDir = new File(getWorkingDirectory(), "temp_" + zipFile.getName());
        JarProcessor processor = new JarProcessor();
        processor.setVerbose(verbose);
        processor.setProcessAll(processAll);
        processor.setWorkingDirectory(tempDir.getCanonicalPath());
        if (unpacking) {
            processor.addProcessStep(unpackStep);
        }
        File outputFile = new File(getWorkingDirectory(), zipFile.getName() + ".temp");
        File parent = outputFile.getParentFile();
        if (!parent.exists()) parent.mkdirs();
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
        Enumeration entries = zip.entries();
        if (entries.hasMoreElements()) {
            for (ZipEntry entry = (ZipEntry) entries.nextElement(); entry != null; entry = entries.hasMoreElements() ? (ZipEntry) entries.nextElement() : null) {
                String name = entry.getName();
                InputStream entryStream = zip.getInputStream(entry);
                boolean pack = packing && !packExclusions.contains(name);
                boolean sign = signing && !signExclusions.contains(name);
                boolean repack = repacking && !packExclusions.contains(name);
                File extractedFile = null;
                if (entry.getName().endsWith(extension) && (pack || sign || repack || unpacking)) {
                    extractedFile = new File(tempDir, name);
                    parent = extractedFile.getParentFile();
                    if (!parent.exists()) parent.mkdirs();
                    if (verbose) System.out.println("Extracting " + entry.getName());
                    FileOutputStream extracted = new FileOutputStream(extractedFile);
                    Utils.transferStreams(entryStream, extracted, true);
                    entryStream = null;
                    boolean skip = Utils.shouldSkipJar(extractedFile, processAll, verbose);
                    if (skip) {
                        entryStream = new FileInputStream(extractedFile);
                        if (verbose) System.out.println(entry.getName() + " is not marked, skipping.");
                    } else {
                        if (unpacking) {
                            File result = processor.processJar(extractedFile);
                            name = name.substring(0, name.length() - extractedFile.getName().length()) + result.getName();
                            extractedFile = result;
                        } else {
                            if (repack || sign) {
                                processor.clearProcessSteps();
                                if (repack) processor.addProcessStep(packUnpackStep);
                                if (sign) processor.addProcessStep(signStep);
                                extractedFile = processor.processJar(extractedFile);
                            }
                            if (pack) {
                                processor.clearProcessSteps();
                                processor.addProcessStep(packStep);
                                File modifiedFile = processor.processJar(extractedFile);
                                if (modifiedFile.exists()) {
                                    try {
                                        String newName = name.substring(0, name.length() - extractedFile.getName().length()) + modifiedFile.getName();
                                        if (verbose) {
                                            System.out.println("Adding " + newName + " to " + outputFile.getPath());
                                            System.out.println();
                                        }
                                        ZipEntry zipEntry = new ZipEntry(newName);
                                        entryStream = new FileInputStream(modifiedFile);
                                        zipOut.putNextEntry(zipEntry);
                                        Utils.transferStreams(entryStream, zipOut, false);
                                        entryStream.close();
                                        Utils.clear(modifiedFile);
                                    } catch (IOException e) {
                                        Utils.close(entryStream);
                                        if (verbose) {
                                            e.printStackTrace();
                                            System.out.println("Warning: Problem reading " + modifiedFile.getPath() + ".");
                                        }
                                    }
                                    entryStream = null;
                                } else if (verbose) {
                                    System.out.println("Warning: " + modifiedFile.getPath() + " not found.");
                                }
                            }
                        }
                        if (extractedFile.exists()) {
                            try {
                                entryStream = new FileInputStream(extractedFile);
                            } catch (IOException e) {
                                if (verbose) {
                                    e.printStackTrace();
                                    System.out.println("Warning: Problem reading " + extractedFile.getPath() + ".");
                                }
                            }
                        }
                        if (verbose && entryStream != null) {
                            System.out.println("Adding " + name + " to " + outputFile.getPath());
                        }
                    }
                }
                if (entryStream != null) {
                    ZipEntry newEntry = new ZipEntry(name);
                    try {
                        zipOut.putNextEntry(newEntry);
                        Utils.transferStreams(entryStream, zipOut, false);
                        zipOut.closeEntry();
                    } catch (ZipException e) {
                        if (verbose) {
                            System.out.println("Warning: " + name + " already exists in " + outputFile.getName() + ".  Skipping.");
                        }
                    }
                    entryStream.close();
                }
                if (extractedFile != null) Utils.clear(extractedFile);
                if (verbose) {
                    System.out.println();
                    System.out.println("Processing " + zipFile.getPath());
                }
            }
        }
        zipOut.close();
        zip.close();
        File finalFile = new File(getWorkingDirectory(), zipFile.getName());
        if (finalFile.exists()) finalFile.delete();
        outputFile.renameTo(finalFile);
        Utils.clear(tempDir);
    }
