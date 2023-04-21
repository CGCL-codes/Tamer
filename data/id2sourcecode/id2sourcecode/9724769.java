                    public void respond(RequestCycle requestCycle) {
                        boolean success = false;
                        OutputStream out = null;
                        try {
                            Response response = requestCycle.getResponse();
                            byte[] buf = new byte[WebCfgDelegate.getInstance().getDefaultFolderPagesize()];
                            HashSet<Integer> sopHash = new HashSet<Integer>();
                            if (files.size() > 1) {
                                response.setContentType("application/zip");
                                ((WebResponse) response).setAttachmentHeader("dicom.zip");
                                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                                out = zos;
                                for (FileToExport fto : files) {
                                    log.debug("Write file to zip:{}", fto.file);
                                    ZipEntry entry = new ZipEntry(getZipEntryName(fto.blobAttrs, sopHash));
                                    zos.putNextEntry(entry);
                                    writeDicomFile(fto.file, fto.blobAttrs, zos, buf);
                                    zos.closeEntry();
                                }
                            } else {
                                response.setContentType("application/dicom");
                                ((WebResponse) response).setAttachmentHeader(getTemplateParam(files.get(0).blobAttrs, "#sopIuid", sopHash) + ".dcm");
                                out = response.getOutputStream();
                                writeDicomFile(files.get(0).file, files.get(0).blobAttrs, out, buf);
                            }
                            success = true;
                        } catch (ZipException ze) {
                            log.warn("Problem creating zip file: " + ze);
                        } catch (ClientAbortException cae) {
                            log.warn("Client aborted zip file download: " + cae);
                        } catch (Exception e) {
                            log.error("An error occurred while attempting to stream zip file for download: ", e);
                        } finally {
                            logExport(files, success);
                            try {
                                if (out != null) out.close();
                            } catch (Exception ignore) {
                            }
                        }
                    }
