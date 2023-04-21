    private Image createImage() {
        Image bi = null;
        log = "";
        try {
            final String code = shape.getText();
            if (code != null && !code.isEmpty()) {
                File tmpDir = LFileUtils.INSTANCE.createTempDir();
                final String doc = getLaTeXDocument();
                pathPic = tmpDir.getAbsolutePath() + LResources.FILE_SEP + "latexdrawTmpPic" + System.currentTimeMillis();
                final String pathTex = pathPic + TeXFilter.TEX_EXTENSION;
                final FileOutputStream fos = new FileOutputStream(pathTex);
                final OutputStreamWriter osw = new OutputStreamWriter(fos);
                RandomAccessFile raf = null;
                FileChannel fc = null;
                final OperatingSystem os = LSystem.INSTANCE.getSystem();
                try {
                    osw.append(doc);
                    try {
                        osw.close();
                    } catch (final IOException ex) {
                        BadaboomCollector.INSTANCE.add(ex);
                    }
                    try {
                        fos.close();
                    } catch (final IOException ex) {
                        BadaboomCollector.INSTANCE.add(ex);
                    }
                    boolean ok = execute(new String[] { os.getLatexBinPath(), "--halt-on-error", "--interaction=nonstopmode", "--output-directory=" + tmpDir.getAbsolutePath(), pathTex });
                    new File(pathTex).delete();
                    new File(pathPic + ".aux").delete();
                    new File(pathPic + ".log").delete();
                    if (ok) {
                        ok = execute(new String[] { os.getDvipsBinPath(), pathPic + ".dvi", "-o", pathPic + PSFilter.PS_EXTENSION });
                        new File(pathPic + ".dvi").delete();
                    }
                    if (ok) ok = execute(new String[] { os.getPs2pdfBinPath(), pathPic + PSFilter.PS_EXTENSION, pathPic + PDFFilter.PDF_EXTENSION });
                    if (ok) try {
                        raf = new RandomAccessFile(new File(pathPic + PDFFilter.PDF_EXTENSION), "r");
                        fc = raf.getChannel();
                        final PDFFile pdfFile = new PDFFile(fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()));
                        if (pdfFile.getNumPages() == 1) {
                            final PDFPage page = pdfFile.getPage(1);
                            final Rectangle2D bound = page.getBBox();
                            final Image img = page.getImage((int) bound.getWidth(), (int) bound.getHeight(), bound, null, false, true);
                            if (img instanceof BufferedImage) bi = ImageCropper.INSTANCE.cropImage((BufferedImage) img);
                            if (img != null) img.flush();
                        } else BadaboomCollector.INSTANCE.add(new IllegalArgumentException("Not a single page: " + pdfFile.getNumPages()));
                        new File(pathPic + PDFFilter.PDF_EXTENSION).delete();
                    } catch (Exception ex) {
                        BadaboomCollector.INSTANCE.add(ex);
                    }
                } catch (final IOException ex) {
                    BadaboomCollector.INSTANCE.add(ex);
                }
                try {
                    if (fc != null) fc.close();
                } catch (IOException ex) {
                    BadaboomCollector.INSTANCE.add(ex);
                }
                try {
                    if (raf != null) raf.close();
                } catch (IOException ex) {
                    BadaboomCollector.INSTANCE.add(ex);
                }
                try {
                    osw.close();
                } catch (final IOException ex) {
                    BadaboomCollector.INSTANCE.add(ex);
                }
                try {
                    fos.close();
                } catch (final IOException ex) {
                    BadaboomCollector.INSTANCE.add(ex);
                }
            }
        } catch (Exception e) {
            new File(pathPic + TeXFilter.TEX_EXTENSION).delete();
            new File(pathPic + PDFFilter.PDF_EXTENSION).delete();
            new File(pathPic + PSFilter.PS_EXTENSION).delete();
            new File(pathPic + ".dvi").delete();
            new File(pathPic + ".aux").delete();
            new File(pathPic + ".log").delete();
            BadaboomCollector.INSTANCE.add(new FileNotFoundException(log + e.getMessage()));
        }
        return bi;
    }
