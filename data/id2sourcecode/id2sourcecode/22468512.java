    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Files in a supported MegaMek file format can be specified on");
            System.out.println("the command line.  Multiple files may be processed at once.");
            System.out.println("The supported formats are:");
            System.out.println("\t.mtf    The native MegaMek format that your file will be converted into");
            System.out.println("\t.blk    Another native MegaMek format");
            System.out.println("\t.hmp    Heavy Metal Pro (c)RCW Enterprises");
            System.out.println("\t.mep    MechEngineer Pro (c)Howling Moon SoftWorks");
            System.out.println("\t.xml    The Drawing Board (c)Blackstone Interactive");
            System.out.println("Note: If you are using the MtfConvert utility, you may also drag and drop files onto it for conversion.");
            MechFileParser.getResponse("Press <enter> to exit...");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String filename = args[i];
            File file = new File(filename);
            String outFilename = filename.substring(0, filename.lastIndexOf("."));
            BufferedWriter out = null;
            try {
                MechFileParser mfp = new MechFileParser(file);
                Entity e = mfp.getEntity();
                if (e instanceof Mech) {
                    outFilename += ".mtf";
                    File outFile = new File(outFilename);
                    if (outFile.exists()) {
                        if (!MechFileParser.getResponse("File already exists, overwrite? ")) {
                            return;
                        }
                    }
                    out = new BufferedWriter(new FileWriter(outFile));
                    out.write(((Mech) e).getMtf());
                } else if (e instanceof Tank) {
                    outFilename += ".blk";
                    BLKFile.encode(outFilename, e);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                MechFileParser.getResponse("Press <enter> to exit...");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }
