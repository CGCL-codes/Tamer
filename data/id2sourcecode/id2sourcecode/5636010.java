    protected void handlePut(Request request, Response response, File file, String entryName) {
        boolean zipExists = file.exists();
        ZipOutputStream zipOut = null;
        if ("".equals(entryName) && request.getEntity() != null && request.getEntity().getDisposition() != null) {
            entryName = request.getEntity().getDisposition().getFilename();
        }
        if (entryName == null) {
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Must specify an entry name.");
            return;
        }
        boolean canAppend = !zipExists;
        boolean isDirectory = entryName.endsWith("/");
        boolean wrongReplace = false;
        try {
            if (zipExists) {
                ZipFile zipFile = new ZipFile(file);
                canAppend &= null == zipFile.getEntry(entryName);
                if (isDirectory) {
                    wrongReplace = null != zipFile.getEntry(entryName.substring(0, entryName.length() - 1));
                } else {
                    wrongReplace = null != zipFile.getEntry(entryName + "/");
                }
                canAppend &= !wrongReplace;
                zipFile.close();
            }
            Representation entity;
            if (isDirectory) {
                entity = null;
            } else {
                entity = request.getEntity();
            }
            if (canAppend) {
                try {
                    zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    writeEntityStream(entity, zipOut, entryName);
                    zipOut.close();
                } catch (Exception e) {
                    response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
                    return;
                } finally {
                    if (zipOut != null) zipOut.close();
                }
                response.setStatus(Status.SUCCESS_CREATED);
            } else {
                if (wrongReplace) {
                    response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Directory cannot be replace by a file or file by a directory.");
                } else {
                    File writeTo = null;
                    ZipFile zipFile = null;
                    try {
                        writeTo = File.createTempFile("restlet_zip_", "zip");
                        zipFile = new ZipFile(file);
                        zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(writeTo)));
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        boolean replaced = false;
                        while (entries.hasMoreElements()) {
                            ZipEntry e = entries.nextElement();
                            if (!replaced && entryName.equals(e.getName())) {
                                writeEntityStream(entity, zipOut, entryName);
                                replaced = true;
                            } else {
                                zipOut.putNextEntry(e);
                                BioUtils.copy(new BufferedInputStream(zipFile.getInputStream(e)), zipOut);
                                zipOut.closeEntry();
                            }
                        }
                        if (!replaced) {
                            writeEntityStream(entity, zipOut, entryName);
                        }
                        zipFile.close();
                        zipOut.close();
                    } finally {
                        try {
                            if (zipFile != null) zipFile.close();
                        } finally {
                            if (zipOut != null) zipOut.close();
                        }
                    }
                    if (!(BioUtils.delete(file) && writeTo.renameTo(file))) {
                        if (!file.exists()) file.createNewFile();
                        FileInputStream fis = null;
                        FileOutputStream fos = null;
                        try {
                            fis = new FileInputStream(writeTo);
                            fos = new FileOutputStream(file);
                            BioUtils.copy(fis, fos);
                            response.setStatus(Status.SUCCESS_OK);
                        } finally {
                            try {
                                if (fis != null) fis.close();
                            } finally {
                                if (fos != null) fos.close();
                            }
                        }
                    } else {
                        response.setStatus(Status.SUCCESS_OK);
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
            return;
        }
    }
