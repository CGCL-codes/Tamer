    public static void createSet(String sourceDirectory, String setName) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("Creating pngs from set " + sourceDirectory + "..." + " set is named " + setName);
        for (int i = 8; i <= 100; i += 2) {
            ProcessBuilder builder = new ProcessBuilder(new String[] { "java", "-jar", "batik-1.7/batik-rasterizer.jar", "-d", "target/" + setName + "/" + i, "-w", "" + i, "-h", "" + i, getSVGChessPieceName(sourceDirectory, 1), getSVGChessPieceName(sourceDirectory, 2), getSVGChessPieceName(sourceDirectory, 3), getSVGChessPieceName(sourceDirectory, 4), getSVGChessPieceName(sourceDirectory, 5), getSVGChessPieceName(sourceDirectory, 6), getSVGChessPieceName(sourceDirectory, 7), getSVGChessPieceName(sourceDirectory, 8), getSVGChessPieceName(sourceDirectory, 9), getSVGChessPieceName(sourceDirectory, 10), getSVGChessPieceName(sourceDirectory, 11), getSVGChessPieceName(sourceDirectory, 12) });
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
            FileUtils.copyFiles(new File(sourceDirectory), new File("target/" + setName));
            System.out.println("Created pngs of size " + i + "x" + i);
        }
        System.out.println("Finished set conversion in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
        System.out.println("The chess set directory is located at: " + "target/" + setName);
    }
