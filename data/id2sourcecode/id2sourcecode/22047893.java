    public static void main(String[] args) throws PdfRecompressionException {
        if (args.length < 4) {
            usage();
        }
        String jbig2enc = null;
        String pdfFile = null;
        String outputPdf = null;
        String password = null;
        Double defaultThresh = 0.85;
        Integer bwThresh = 188;
        Boolean autoThresh = false;
        Set<Integer> pagesToProcess = null;
        Boolean silent = false;
        Boolean binarize = false;
        boolean useOcr = false;
        String basename = System.getProperty("java.io.tmpdir") + "/output";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-h")) {
                usage();
            }
            if (args[i].equalsIgnoreCase("-input")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                pdfFile = args[i];
                continue;
            }
            if (args[i].equalsIgnoreCase("-pathToEnc")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                jbig2enc = args[i];
                continue;
            }
            if (args[i].equalsIgnoreCase("-output")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                outputPdf = args[i];
                continue;
            }
            if (args[i].equalsIgnoreCase("-passwd")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                password = args[i];
                continue;
            }
            if (args[i].equalsIgnoreCase("-basename")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                basename = args[i];
                continue;
            }
            if (args[i].equalsIgnoreCase("-thresh")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                defaultThresh = Double.parseDouble(args[i]);
                if ((defaultThresh > 0.9) || (defaultThresh < 0.5)) {
                    System.err.println("Invalid threshold value: (0.5..0.9)\n");
                    usage();
                }
                continue;
            }
            if (args[i].equalsIgnoreCase("-bw_thresh")) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                bwThresh = Integer.parseInt(args[i]);
                if ((bwThresh < 0) || (bwThresh > 255)) {
                    System.err.println("Invalid bw threshold value: (0..255)\n");
                    usage();
                }
                continue;
            }
            if (args[i].equalsIgnoreCase("-binarize")) {
                binarize = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("-autoThresh")) {
                autoThresh = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("-useOcr")) {
                useOcr = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("-q")) {
                silent = true;
                continue;
            }
            if (args[i].equalsIgnoreCase("-pages")) {
                pagesToProcess = new HashSet<Integer>();
                i++;
                if (i >= args.length) {
                    usage();
                }
                try {
                    while (!args[i].equalsIgnoreCase("-pagesEnd")) {
                        int page = Integer.parseInt(args[i]);
                        pagesToProcess.add(page);
                        i++;
                        if (i >= args.length) {
                            usage();
                        }
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("list of page numbers can contain only numbers");
                    usage();
                }
                continue;
            }
        }
        if ((jbig2enc == null) || (pdfFile == null)) {
            usage();
        }
        if (outputPdf == null) {
            outputPdf = pdfFile;
        }
        File originalPdf = new File(pdfFile);
        long sizeOfInputPdf = new File(pdfFile).length();
        double startTime = System.currentTimeMillis();
        PdfImageExtractor imageExtractor = new PdfImageExtractor();
        imageExtractor.extractImages(pdfFile, password, pagesToProcess, binarize);
        List<String> jbig2encInputImages = imageExtractor.getNamesOfImages();
        if (jbig2encInputImages.isEmpty()) {
            if (!silent) {
                log.info("No images in " + pdfFile + " to recompress");
            }
        } else {
            Jbig2enc jbig2 = new Jbig2enc(jbig2enc);
            jbig2.setAutoThresh(autoThresh);
            jbig2.setBwThresh(bwThresh);
            jbig2.setDefaultThresh(defaultThresh);
            jbig2.setUseOcr(useOcr);
            jbig2.run(jbig2encInputImages, basename);
        }
        List<PdfImageInformation> pdfImagesInfo = imageExtractor.getOriginalImageInformations();
        int lastPathSeparator = basename.lastIndexOf(File.separator);
        String basenameDir = ".";
        if (lastPathSeparator != -1) {
            basenameDir = basename.substring(0, lastPathSeparator);
            basename = basename.substring(lastPathSeparator + 1);
        }
        log.debug("basename dir = {} and basename = {}", basenameDir, basename);
        Jbig2ForPdf pdfImages = new Jbig2ForPdf(basenameDir, basename);
        pdfImages.setJbig2ImagesInfo(pdfImagesInfo);
        OutputStream out = null;
        try {
            File fileName = new File(outputPdf);
            if (fileName.createNewFile()) {
                if (!silent) {
                    log.info("file " + outputPdf + " was created");
                }
            } else {
                if (!silent) {
                    log.info("file " + outputPdf + " already exist => will be rewriten");
                }
            }
            out = new FileOutputStream(fileName);
            PdfImageReplacer imageReplacer = new PdfImageReplacer();
            imageReplacer.replaceImageUsingIText(pdfFile, out, pdfImages);
            long sizeOfOutputPdf = fileName.length();
            float saved = (((float) (sizeOfInputPdf - sizeOfOutputPdf)) / sizeOfInputPdf) * 100;
            if (!silent) {
                log.info("Size of pdf before recompression = {}", sizeOfInputPdf);
                log.info("Size of pdf file after recompression = {}", sizeOfOutputPdf);
                log.info("=> Saved {} % from original size", String.format("%.2f", saved));
            }
        } catch (IOException ex) {
            log.warn("writing output to the file caused error", ex);
            System.exit(2);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex2) {
                }
            }
        }
        int time = (int) (System.currentTimeMillis() - startTime) / 1000;
        int hour = time / 3600;
        int min = (time % 3600) / 60;
        int sec = (time % 3600) % 60;
        log.info("{} succesfully recompressed in {}", pdfFile, String.format("%02d:%02d:%02d", hour, min, sec));
        log.info("Totaly was recompressed {} images", pdfImages.getMapOfJbig2Images().size());
    }
