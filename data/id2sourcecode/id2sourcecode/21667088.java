    public static void createSquares(String sourceDirectory, String squareName) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("Creating pngs from squareDirectory " + sourceDirectory + "..." + " it will be named " + squareName);
        for (int i = 10; i <= 200; i += 1) {
            ProcessBuilder builder = new ProcessBuilder(new String[] { "java", "-jar", "batik-1.7/batik-rasterizer.jar", "-d", "target/" + squareName + "/" + i, "-w", "" + i, "-h", "" + i, sourceDirectory + "/dark.svg", sourceDirectory + "/light.svg" });
            Process process = builder.start();
            Thread outputDumper = new Thread(new OutputStreamDumper(process.getInputStream(), System.out));
            Thread errorDumper = new Thread(new OutputStreamDumper(process.getErrorStream(), System.err));
            outputDumper.start();
            errorDumper.start();
            int error = process.waitFor();
            if (error != 0) {
                System.err.println("Stopped due to error.");
                System.exit(1);
            }
            FileUtils.copyFiles(new File(sourceDirectory), new File("target/" + squareName));
            System.out.println("Created pngs of size " + i + "x" + i);
        }
        System.out.println("Finished set conversion in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
        System.out.println("The chess set directory is located at: " + "target/" + squareName);
    }
