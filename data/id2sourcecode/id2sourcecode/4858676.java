    @Override
    public void run() {
        boolean verbose;
        int i;
        String infile;
        RandomAccessIO in;
        String outfile = "", outbase = "", outext = "";
        String out[] = null;
        ImgWriter imwriter[] = null;
        boolean disp = false;
        Image img = null;
        Dimension winDim, scrnDim;
        Insets ins = null;
        String btitle = "";
        try {
            try {
                if (pl.getBooleanParameter("v")) {
                    printVersionAndCopyright();
                }
                if (pl.getParameter("u").equals("on")) {
                    printUsage();
                    return;
                }
                verbose = pl.getBooleanParameter("verbose");
            } catch (StringFormatException e) {
                error("An error occured while parsing the arguments:\n" + e.getMessage(), 1, e);
                return;
            } catch (NumberFormatException e) {
                error("An error occured while parsing the arguments:\n" + e.getMessage(), 1, e);
                return;
            }
            try {
                pl.checkList(vprfxs, ParameterList.toNameArray(pinfo));
            } catch (IllegalArgumentException e) {
                error(e.getMessage(), 2, e);
                return;
            }
            infile = pl.getParameter("i");
            if (infile == null) {
                error("Input file ('-i' option) has not been specified", 1);
                return;
            }
            outfile = pl.getParameter("o");
            if (outfile == null) {
                disp = true;
            } else if (outfile.lastIndexOf('.') != -1) {
                outext = outfile.substring(outfile.lastIndexOf('.'), outfile.length());
                outbase = outfile.substring(0, outfile.lastIndexOf('.'));
            } else {
                outbase = outfile;
                outext = ".pgx";
            }
            if (infile.indexOf("/") >= 1 && infile.charAt(infile.indexOf("/") - 1) == ':') {
                URL inurl;
                URLConnection conn;
                int datalen;
                InputStream is;
                try {
                    inurl = new URL(infile);
                } catch (MalformedURLException e) {
                    error("Malformed URL for input file " + infile, 4, e);
                    return;
                }
                try {
                    conn = inurl.openConnection();
                    conn.connect();
                } catch (IOException e) {
                    error("Cannot open connection to " + infile + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 4, e);
                    return;
                }
                datalen = conn.getContentLength();
                try {
                    is = conn.getInputStream();
                } catch (IOException e) {
                    error("Cannot get data from connection to " + infile + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 4, e);
                    return;
                }
                if (datalen != -1) {
                    in = new ISRandomAccessIO(is, datalen, 1, datalen);
                } else {
                    in = new ISRandomAccessIO(is);
                }
                try {
                    in.read();
                    in.seek(0);
                } catch (IOException e) {
                    error("Cannot get input data from " + infile + " Invalid URL?", 4, e);
                    return;
                }
            } else {
                try {
                    in = new BEBufferedRandomAccessFile(infile, "r");
                } catch (IOException e) {
                    error("Cannot open input file " + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 4, e);
                    return;
                }
            }
            FileFormatReader ff = new FileFormatReader(in);
            ff.readFileFormat();
            if (ff.JP2FFUsed) {
                in.seek(ff.getFirstCodeStreamPos());
            }
            BlkImgDataSrc decodedImage = decode(in, ff, verbose);
            int nCompImg = decodedImage.getNumComps();
            if (disp) {
                btitle = "JJ2000: " + (new File(infile)).getName() + " " + decodedImage.getImgWidth() + "x" + decodedImage.getImgHeight();
                if (isp == null) {
                    win = new Frame(btitle + " @ (0,0) : 1");
                    win.setBackground(Color.white);
                    win.addWindowListener(new ExitHandler(this));
                    isp = new ImgScrollPane(ImgScrollPane.SCROLLBARS_AS_NEEDED);
                    win.add(isp, BorderLayout.CENTER);
                    isp.addKeyListener(new ImgKeyListener(isp, this));
                    win.addKeyListener(new ImgKeyListener(isp, this));
                } else {
                    win = null;
                }
                if (win != null) {
                    win.addNotify();
                    ins = win.getInsets();
                    int subX = decodedImage.getCompSubsX(0);
                    int subY = decodedImage.getCompSubsY(0);
                    int w = (decodedImage.getImgWidth() + subX - 1) / subX;
                    int h = (decodedImage.getImgHeight() + subY - 1) / subY;
                    winDim = new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
                    scrnDim = win.getToolkit().getScreenSize();
                    if (winDim.width > scrnDim.width * 8 / 10f) {
                        winDim.width = (int) (scrnDim.width * 8 / 10f);
                    }
                    if (winDim.height > scrnDim.height * 8 / 10f) {
                        winDim.height = (int) (scrnDim.height * 8 / 10f);
                    }
                    win.setSize(winDim);
                    win.validate();
                    win.setVisible(true);
                    Thread tu;
                    title = new TitleUpdater(isp, win, btitle);
                    tu = new Thread(title);
                    tu.start();
                } else {
                    title = null;
                }
            } else {
                if (csMap != null) {
                    if (outext.equalsIgnoreCase(".PPM") && (nCompImg != 3 || decodedImage.getNomRangeBits(0) > 8 || decodedImage.getNomRangeBits(1) > 8 || decodedImage.getNomRangeBits(2) > 8 || csMap.isOutputSigned(0) || csMap.isOutputSigned(1) || csMap.isOutputSigned(2))) {
                        error("Specified PPM output file but compressed image is not of the correct format " + "for PPM or limited decoded components to less than 3.", 1);
                        return;
                    }
                } else {
                    if (outext.equalsIgnoreCase(".PPM") && (nCompImg != 3 || decodedImage.getNomRangeBits(0) > 8 || decodedImage.getNomRangeBits(1) > 8 || decodedImage.getNomRangeBits(2) > 8 || hd.isOriginalSigned(0) || hd.isOriginalSigned(1) || hd.isOriginalSigned(2))) {
                        error("Specified PPM output file but compressed image is not of the correct format " + "for PPM or limited decoded components to less than 3.", 1);
                        return;
                    }
                }
                out = new String[nCompImg];
                for (i = 0; i < nCompImg; i++) {
                    out[i] = "";
                }
                if (nCompImg > 1 && !outext.equalsIgnoreCase(".PPM")) {
                    if (outext.equalsIgnoreCase(".PGM")) {
                        for (i = 0; i < nCompImg; i++) {
                            if (csMap != null) {
                                if (csMap.isOutputSigned(i)) {
                                    error("Specified PGM output file but compressed image is not of the " + "correct format for PGM.", 1);
                                    return;
                                }
                            } else {
                                if (hd.isOriginalSigned(i)) {
                                    error("Specified PGM output file but compressed image is not of the " + "correct format for PGM.", 1);
                                    return;
                                }
                            }
                        }
                    }
                    for (i = 0; i < nCompImg; i++) {
                        out[i] = outbase + "-" + (i + 1) + outext;
                    }
                } else {
                    out[0] = outbase + outext;
                }
                if (outext.equalsIgnoreCase(".PPM")) {
                    imwriter = new ImgWriter[1];
                    try {
                        imwriter[0] = new ImgWriterPPM(out[0], decodedImage, 0, 1, 2);
                    } catch (IOException e) {
                        error("Cannot write PPM header or open output file" + i + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 2, e);
                        return;
                    }
                } else {
                    imwriter = new ImgWriter[nCompImg];
                }
                if (csMap != null) {
                    if (imwriter.length == 3 && decodedImage.getNomRangeBits(0) <= 8 && decodedImage.getNomRangeBits(1) <= 8 && decodedImage.getNomRangeBits(2) <= 8 && !csMap.isOutputSigned(0) && !csMap.isOutputSigned(1) && !csMap.isOutputSigned(2) && decSpec.cts.isCompTransfUsed()) {
                        warning("JJ2000 is quicker with one PPM output " + "file than with 3 PGM/PGX output files when a" + " component transformation is applied.");
                    }
                } else {
                    if (imwriter.length == 3 && decodedImage.getNomRangeBits(0) <= 8 && decodedImage.getNomRangeBits(1) <= 8 && decodedImage.getNomRangeBits(2) <= 8 && !hd.isOriginalSigned(0) && !hd.isOriginalSigned(1) && !hd.isOriginalSigned(2) && decSpec.cts.isCompTransfUsed()) {
                        warning("JJ2000 is quicker with one PPM output " + "file than with 3 PGM/PGX output files when a" + " component transformation is applied.");
                    }
                }
            }
            int mrl = decSpec.dls.getMin();
            if (verbose) {
                int res = breader.getImgRes();
                if (mrl != res) {
                    FacilityManager.getMsgLogger().println("Reconstructing resolution " + res + " on " + mrl + " (" + breader.getImgWidth(res) + "x" + breader.getImgHeight(res) + ")", 8, 8);
                }
                if (pl.getFloatParameter("rate") != -1) {
                    FacilityManager.getMsgLogger().println("Target rate = " + breader.getTargetRate() + " bpp (" + breader.getTargetNbytes() + " bytes)", 8, 8);
                }
            }
            if (disp) {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY + 1);
                img = BlkImgDataSrcImageProducer.createImage(decodedImage, isp);
                isp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (win != null) {
                    win.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
                isp.setImage(img);
                isp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (win != null) {
                    win.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                if (win != null) {
                    int status;
                    do {
                        status = isp.checkImage(img, null);
                        if ((status & ImageObserver.ERROR) != 0) {
                            FacilityManager.getMsgLogger().printmsg(MsgLogger.ERROR, "An unknown error occurred while producing the image");
                            return;
                        } else if ((status & ImageObserver.ABORT) != 0) {
                            FacilityManager.getMsgLogger().printmsg(MsgLogger.ERROR, "Image production was aborted for some unknown reason");
                        } else if ((status & ImageObserver.ALLBITS) != 0) {
                            ImgMouseListener iml = new ImgMouseListener(isp);
                            isp.addMouseListener(iml);
                            isp.addMouseMotionListener(iml);
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                        }
                    } while ((status & (ImageObserver.ALLBITS | ImageObserver.ABORT | ImageObserver.ERROR)) == 0);
                }
            } else {
                for (i = 0; i < imwriter.length; i++) {
                    if (outext.equalsIgnoreCase(".PGM")) {
                        try {
                            imwriter[i] = new ImgWriterPGM(out[i], decodedImage, i);
                        } catch (IOException e) {
                            error("Cannot write PGM header or open output file for component " + i + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 2, e);
                            return;
                        }
                    } else if (outext.equalsIgnoreCase(".PGX")) {
                        try {
                            if (csMap != null) {
                                imwriter[i] = new ImgWriterPGX(out[i], decodedImage, i, csMap.isOutputSigned(i));
                            } else {
                                imwriter[i] = new ImgWriterPGX(out[i], decodedImage, i, hd.isOriginalSigned(i));
                            }
                        } catch (IOException e) {
                            error("Cannot write PGX header or open output file for component " + i + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 2, e);
                            return;
                        }
                    }
                    try {
                        imwriter[i].writeAll();
                    } catch (IOException e) {
                        error("I/O error while writing output file" + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 2, e);
                        return;
                    }
                    try {
                        imwriter[i].close();
                    } catch (IOException e) {
                        error("I/O error while closing output file (data may be corrupted" + ((e.getMessage() != null) ? (":\n" + e.getMessage()) : ""), 2, e);
                        return;
                    }
                }
            }
            if (verbose) {
                float bitrate = breader.getActualRate();
                int numBytes = breader.getActualNbytes();
                if (ff.JP2FFUsed) {
                    int imageSize = (int) ((8.0f * numBytes) / bitrate);
                    numBytes += ff.getFirstCodeStreamPos();
                    bitrate = (numBytes * 8.0f) / imageSize;
                }
                if (pl.getIntParameter("ncb_quit") == -1) {
                    FacilityManager.getMsgLogger().println("Actual bitrate = " + bitrate + " bpp (i.e. " + numBytes + " bytes)", 8, 8);
                } else {
                    FacilityManager.getMsgLogger().println("Number of packet body bytes read = " + numBytes, 8, 8);
                }
                FacilityManager.getMsgLogger().flush();
            }
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 2);
            if (pl.getParameter("debug").equals("on")) e.printStackTrace();
            return;
        } catch (Error e) {
            if (e.getMessage() != null) {
                error(e.getMessage(), 2);
            } else {
                error("An error has occured during decoding.", 2);
            }
            if (pl.getParameter("debug").equals("on")) {
                e.printStackTrace();
            } else {
                error("Use '-debug' option for more details", 2);
            }
            return;
        } catch (RuntimeException e) {
            if (e.getMessage() != null) {
                error("An uncaught runtime exception has occurred:\n" + e.getMessage(), 2);
            } else {
                error("An uncaught runtime exception has occurred.", 2);
            }
            if (pl.getParameter("debug").equals("on")) {
                e.printStackTrace();
            } else {
                error("Use '-debug' option for more details", 2);
            }
            return;
        } catch (Throwable e) {
            error("An uncaught exception has occurred.", 2);
            if (pl.getParameter("debug").equals("on")) {
                e.printStackTrace();
            } else {
                error("Use '-debug' option for more details", 2);
            }
            return;
        }
    }
