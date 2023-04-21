    public void osdbUploadSubtitles() throws InterruptedException {
        if (uploadOsdb) {
            Logging.logger.finer(Bundles.subgetBundle.getString("Starting_upload_to_OSDb..."));
            CdXmlRpcParam[] cds = new CdXmlRpcParam[vspairs.size()];
            TryUploadXmlRpcParam tryParam = new TryUploadXmlRpcParam();
            UploadXmlRpcParam uploadParam = new UploadXmlRpcParam();
            for (int i = 0; i < cds.length; ++i) {
                FileInputStream fis = null;
                try {
                    byte[] buffer = new byte[1024];
                    fis = new FileInputStream(vspairs.get(i).getSubtitle().getFile());
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    int numread = 0;
                    while ((numread = fis.read(buffer)) != -1) {
                        md5.update(buffer, 0, numread);
                    }
                    fis.close();
                    byte[] hash = md5.digest();
                    StringBuffer hexString = new StringBuffer();
                    for (int j = 0; j < hash.length; j++) {
                        String hexPart;
                        hexPart = Integer.toHexString(0xFF & hash[j]);
                        if (hexPart.length() == 1) {
                            hexPart = "0" + hexPart;
                        }
                        hexString.append(hexPart);
                    }
                    String subhash = hexString.toString();
                    int movietimems = 0;
                    long movieframes = 0;
                    float moviefps = 0;
                    if (vspairs.get(i).getVideo().getHasInfo() == false) {
                        try {
                            vspairs.get(i).getVideo().getVideoInfo();
                        } catch (IOException ex) {
                        } catch (BrokenAviHeaderException ex) {
                        } catch (NotSupportedContainerException ex) {
                        }
                        if (vspairs.get(i).getVideo().getHasInfo()) {
                            movietimems = vspairs.get(i).getVideo().getTime();
                            movieframes = vspairs.get(i).getVideo().getFrames();
                            moviefps = vspairs.get(i).getVideo().getFps();
                        }
                    }
                    cds[i] = new CdXmlRpcParam();
                    cds[i].putSubHash(subhash);
                    cds[i].putSubFilename(vspairs.get(i).getSubtitle().getFile().getName());
                    cds[i].putMovieHash(vspairs.get(i).getVideo().getOsdbHash());
                    cds[i].putMovieBytesize(vspairs.get(i).getVideo().getFile().length());
                    cds[i].putMovieTimeMs(movietimems);
                    cds[i].putMovieFrames(movieframes);
                    cds[i].putMovieFps(moviefps);
                    cds[i].putMovieFilename(vspairs.get(i).getVideo().getFile().getName());
                } catch (IOException ex) {
                    osdbWarnings.add(Bundles.subgetBundle.getString("I/O_error_occured_while_uploading_subtitles."));
                    Logging.logger.severe(Bundles.subgetBundle.getString("I/O_error_occured_while_uploading_subtitles."));
                    return;
                } catch (NoSuchAlgorithmException ex) {
                    osdbWarnings.add(Bundles.subgetBundle.getString("Hashing_error_occured_while_uploading_subtitles."));
                    Logging.logger.severe(Bundles.subgetBundle.getString("Hashing_error_occured_while_uploading_subtitles."));
                    return;
                } finally {
                    try {
                        if (fis != null) fis.close();
                    } catch (IOException ex) {
                        osdbWarnings.add(Bundles.subgetBundle.getString("I/O_error_occured_while_uploading_subtitles."));
                        Logging.logger.severe(Bundles.subgetBundle.getString("I/O_error_occured_while_uploading_subtitles."));
                        return;
                    }
                }
            }
            for (int i = 0; i < cds.length; ++i) {
                tryParam.putCd(i, cds[i]);
            }
            try {
                boolean newSubtitles;
                try {
                    newSubtitles = Osdb.tryUploadSubtitles(tryParam);
                } catch (OsdbException ex) {
                    Global.dialogs.showErrorDialog(Bundles.subgetBundle.getString("Error"), Bundles.subgetBundle.getString("Could_not_upload_subtitles.\n") + ex.getDialogMessage());
                    return;
                }
                if (newSubtitles) {
                    BaseInfoXmlRpcParam baseinfo = new BaseInfoXmlRpcParam();
                    baseinfo.putIdMovieImdb(imdbIDs.get(selectedImdb));
                    baseinfo.putMovieReleaseName(release);
                    baseinfo.putSubLanguageId(Language.xxToxxx(subLanguage));
                    baseinfo.putSubAuthorComment(comments);
                    uploadParam.putBaseInfo(baseinfo);
                    for (int j = 0; j < cds.length; ++j) {
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        DeflaterOutputStream gzout = new DeflaterOutputStream(byteOut);
                        FileInputStream fileIn = new FileInputStream(vspairs.get(j).getSubtitle().getFile());
                        byte[] buffer = new byte[1024];
                        int numRead;
                        while ((numRead = fileIn.read(buffer)) != -1) {
                            gzout.write(buffer, 0, numRead);
                        }
                        gzout.finish();
                        gzout.close();
                        char[] base = Base64.encode(byteOut.toByteArray());
                        String base64String = new String(base);
                        cds[j].putSubContent(base64String);
                        uploadParam.putCd(j, cds[j]);
                    }
                    try {
                        Osdb.uploadSubtitles(uploadParam);
                    } catch (OsdbException ex) {
                        osdbWarnings.add(ex.getDialogMessage());
                        Logging.logger.warning(ex.getDialogMessage());
                        return;
                    }
                } else {
                    osdbWarnings.add(Bundles.subgetBundle.getString("Subtitles_already_are_in_database."));
                    Logging.logger.warning(Bundles.subgetBundle.getString("Subtitles_already_are_in_database."));
                    return;
                }
            } catch (BadLoginException ex) {
                Logger.getLogger(Movie.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                osdbWarnings.add(Bundles.subgetBundle.getString("Connection_error_occured_while_compressing_subtitles."));
                Logging.logger.severe(Bundles.subgetBundle.getString("Connection_error_occured_while_compressing_subtitles."));
            } catch (XmlRpcException ex) {
                osdbWarnings.add(Bundles.subgetBundle.getString("Connection_error_occured_while_uploading_subtitles."));
                Logging.logger.severe(Bundles.subgetBundle.getString("Connection_error_occured_while_compressing_subtitles."));
            } catch (XmlRpcFault ex) {
                osdbWarnings.add(Bundles.subgetBundle.getString("Connection_error_occured_while_uploading_subtitles."));
                Logging.logger.severe(Bundles.subgetBundle.getString("Connection_error_occured_while_compressing_subtitles."));
            } catch (TimeoutException ex) {
                osdbWarnings.add(Bundles.subgetBundle.getString("Connection_error_occured_while_uploading_subtitles,_timeout."));
                Logging.logger.severe(Bundles.subgetBundle.getString("Connection_error_occured_while_compressing_subtitles,_timeout"));
            }
        }
    }
