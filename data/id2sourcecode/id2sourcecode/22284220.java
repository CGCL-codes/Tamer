    public static void main(String[] args) throws PdfRecompressionException {
        if (args.length < 4) {
            usage();
        }
        String jbig2enc = null;
        String pdfFile = null;
        String outputPdf = null;
        String password = null;
        double defaultThresh = 0.85;
        int bwThresh = 188;
        Boolean autoThresh = false;
        Set<Integer> pagesToProcess = null;
        Boolean silent = false;
        Boolean binarize = false;
        String basename = "output";
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
        PdfImageProcessor pdfProcessing = new PdfImageProcessor();
        pdfProcessing.extractImagesUsingPdfParser(pdfFile, password, pagesToProcess, silent, binarize);
        List<String> jbig2encInputImages = pdfProcessing.getNamesOfImages();
        if (jbig2encInputImages.isEmpty()) {
            if (!silent) {
                System.out.println("No images in " + pdfFile + " to recompress");
            }
        }
        Tools.runJbig2enc(jbig2enc, jbig2encInputImages, defaultThresh, autoThresh, bwThresh, basename, silent);
        List<PdfImageInformation> pdfImagesInfo = pdfProcessing.getOriginalImageInformations();
        Jbig2ForPdf pdfImages = new Jbig2ForPdf(".", basename);
        pdfImages.setJbig2ImagesInfo(pdfImagesInfo);
        OutputStream out = null;
        try {
            File fileName = new File(outputPdf);
            if (fileName.createNewFile()) {
                System.out.println("file " + outputPdf + " was created");
            } else {
                System.out.println("file " + outputPdf + " already exist => will be rewriten");
            }
            out = new FileOutputStream(fileName);
            pdfProcessing.replaceImageUsingIText(pdfFile, out, pdfImages, silent);
            long sizeOfOutputPdf = fileName.length();
            float saved = (((float) (sizeOfInputPdf - sizeOfOutputPdf)) / sizeOfInputPdf) * 100;
            System.out.println("Size of pdf before recompression = " + sizeOfInputPdf);
            System.out.println("Size of pdf file after recompression = " + sizeOfOutputPdf);
            System.out.println("=> Saved " + String.format("%.2f", saved) + " % from original size");
        } catch (IOException ex) {
            if (!silent) {
                System.err.println("writing output to the file caused error");
                ex.printStackTrace();
            }
            System.exit(2);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex2) {
                    if (!silent) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
        int time = (int) (System.currentTimeMillis() - startTime) / 1000;
        int hour = time / 3600;
        int min = (time % 3600) / 60;
        int sec = (time % 3600) % 60;
        System.out.print("\n" + pdfFile + " succesfully recompressed in ");
        System.out.println(String.format("%02d:%02d:%02d", hour, min, sec));
        System.out.println("Totaly was recompressed " + pdfImages.getMapOfJbig2Images().size() + " images");
    }
