    public static void main(String[] args) {
        if (args.length < 2) {
            printUsageMessage();
            System.exit(1);
        }
        String outputFileName = null;
        File inputFile = new File(args[0]);
        PlanarImage image = JAI.create("ImageRead", inputFile);
        String arg2 = args[2];
        String fileExtension = null;
        if (arg2.equals(JPEG2000) || arg2.equals(JPEG_2000)) fileExtension = "jp2"; else if (arg2.equals(JPEG_LOSSLESS) || arg2.equals(JPEG_LS)) fileExtension = "jls"; else if (arg2.equals(JPEG)) fileExtension = "jpg"; else if (arg2.equals(TIFF)) fileExtension = "tif"; else fileExtension = arg2;
        if (args.length > 2) {
            File destinationDirectory = new File(args[1]);
            outputFileName = destinationDirectory + File.separator + inputFile.getName().substring(0, inputFile.getName().indexOf('.')) + "." + fileExtension;
            System.out.println("OutputFileName: " + outputFileName);
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdir();
            }
        } else {
            outputFileName = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().indexOf(".")) + "." + fileExtension;
        }
        File outputFile = new File(outputFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            ImageIO.write(image, arg2, out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                image.dispose();
                out.flush();
                out.close();
                FileInputStream fis = new FileInputStream(outputFile);
                if (fis.getChannel().size() > 1) {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.INFO, "Conversion Done!");
                } else {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.WARNING, "Conversion problem encountered, no joy on " + outputFile.getName());
                }
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
