    protected void prepareFile(Field field, MapContentData values, String valalias) {
        ImageInfo fi = (ImageInfo) values.get(valalias + "_info");
        if (fi != null) {
            String ext = fi.getExtension();
            File file = fi.getFile();
            if (file.exists()) {
                try {
                    File workFile = null;
                    AtlantalImage imgTmp;
                    AtlantalImage imgWrk = null;
                    String maxsizestr = field.getParameter("maxsize");
                    if (maxsizestr != null) {
                        int maxsize = Integer.valueOf(maxsizestr).intValue();
                        try {
                            workFile = File.createTempFile("img", "." + ext);
                            scale(file, workFile, maxsize);
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                    }
                    String copyright = field.getParameter("copyright");
                    if (copyright != null) {
                        String cptext = (String) values.get(valalias + "_copyright");
                        if (cptext == null) {
                            cptext = field.getParameter("copyrightdefault");
                        }
                        if (cptext != null) {
                            imgWrk = getAtlantalImage(imgWrk, workFile, file);
                            try {
                                imgTmp = imgWrk;
                                imgWrk = imgTmp.drawCopyright(cptext, true);
                                imgTmp.close();
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        }
                    }
                    String watermark = field.getParameter("watermark");
                    if (watermark != null) {
                        imgWrk = getAtlantalImage(imgWrk, workFile, file);
                        try {
                            imgTmp = imgWrk;
                            imgWrk = imgTmp.drawWatermark(watermark);
                            imgTmp.close();
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                    }
                    File f;
                    if (imgWrk != null) {
                        String type = fi.getType();
                        if ("image/x-png".equals(type)) {
                            f = File.createTempFile("img", ".png");
                            imgWrk.saveAsPNG(f);
                        } else if ("image/gif".equals(type)) {
                            f = File.createTempFile("img", ".gif");
                            imgWrk.saveAsGIF(f);
                        } else {
                            f = File.createTempFile("img", ".jpg");
                            imgWrk.saveAsJPEG(f);
                        }
                        if ((workFile != null) && workFile.exists()) {
                            workFile.delete();
                        }
                    } else {
                        f = File.createTempFile("img", "." + ext);
                        if (workFile != null) {
                            f.delete();
                            workFile.renameTo(f);
                        } else {
                            FileUtils.copyFile(file, f);
                        }
                    }
                    fi.setFile(f);
                    fi.setSize(f.length());
                    if (imgWrk != null) {
                        fi.setWidth(imgWrk.getWidth());
                        fi.setHeight(imgWrk.getHeight());
                        imgWrk.close();
                    } else {
                        ImageInformation ii = new ImageInformation();
                        RandomAccessFile in = new RandomAccessFile(f, "r");
                        ii.setInput(in);
                        if (ii.check()) {
                            fi.setWidth(ii.getWidth());
                            fi.setHeight(ii.getHeight());
                        }
                        in.close();
                    }
                    LOGGER.debug("CREATE >> " + f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }
