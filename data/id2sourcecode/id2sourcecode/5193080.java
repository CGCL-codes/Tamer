    public static void main(String[] args) {
        Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
        if (outputMessages) System.out.println("Simple demo to extract images from a page at various heights");
        if (((args.length & 1) == 0) | (args.length < 5)) {
            LogWriter.writeLog("Values read");
            LogWriter.writeLog("inputDir=" + inputDir);
            LogWriter.writeLog("processedDir=" + processed_dir);
            LogWriter.writeLog("logFile=" + LogWriter.log_name);
            LogWriter.writeLog("Directory and height pair values");
            for (int i = 3; i < outputCount; i++) LogWriter.writeLog(args[i]);
            if ((args.length < 5) | ((args.length & 1) == 0)) {
                System.out.println("Requires");
                System.out.println("inputDir processedDir logFile");
                System.out.println("height Directory (as many pairs as you like)");
                exit("Not enough parameters passed to software");
            } else exit("Incorrect number of values");
        }
        inputDir = args[0];
        processed_dir = args[1];
        File pdf_file = new File(inputDir);
        File processedDir = new File(processed_dir);
        if (!processedDir.exists()) processedDir.mkdirs();
        if (pdf_file.exists() == false) exit("Directory " + inputDir + " not found");
        outputCount = (args.length - 3) / 2;
        outputSizes = new float[outputCount];
        outputDirectories = new String[outputCount];
        for (int i = 0; i < outputCount; i++) {
            try {
                outputSizes[i] = Float.parseFloat(args[3 + (i * 2)]);
            } catch (Exception e) {
                exit("Exception " + e + " reading integer " + args[3 + (i * 2)]);
            }
            try {
                outputDirectories[i] = args[4 + (i * 2)];
                if ((!outputDirectories[i].endsWith("\\")) && (!outputDirectories[i].endsWith("/"))) outputDirectories[i] = outputDirectories[i] + separator;
                File dir = new File(outputDirectories[i]);
                if (!dir.exists()) dir.mkdirs();
            } catch (Exception e) {
                exit("Exception " + e + " with directory " + args[4 + (i * 2)]);
            }
        }
        ExtractClippedImages images1 = new ExtractClippedImages(inputDir);
        LogWriter.writeLog("Process completed");
    }
