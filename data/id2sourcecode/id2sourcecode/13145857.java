    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        if (args.length == 0) {
            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            System.out.println(fonts.length + " Available font families : ");
            for (String fontname : fonts) {
                System.out.println("\t" + fontname);
            }
            System.out.println("usage: \"java -jar FontFactory.jar (-d) <fontname>-<size>\"");
        } else {
            int i = 0;
            boolean distanceField = false;
            if (args[0].equals("-d")) {
                distanceField = true;
                i = 1;
            }
            for (; i < args.length; i++) {
                try {
                    System.out.println("Converting " + args[i] + "...");
                    java.awt.Font src = java.awt.Font.decode(args[i]);
                    FontFactory fc = new FontFactory(src.getFontName() + "-" + src.getSize(), distanceField);
                    fc.listener = new CommandLineListener();
                    Font f = fc.buildFont(defaultCharSet);
                    try {
                        String outputName = src.getFontName() + "_" + src.getSize() + ".ruglfont";
                        outputName = outputName.replaceAll(" ", "_");
                        outputName = outputName.toLowerCase();
                        System.out.println("\twriting to " + outputName);
                        RandomAccessFile rf = new RandomAccessFile(outputName, "rw");
                        FileChannel ch = rf.getChannel();
                        int fileLength = f.dataSize();
                        rf.setLength(fileLength);
                        MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
                        f.write(buffer);
                        buffer.force();
                        ch.close();
                        System.out.println("\tdone");
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
