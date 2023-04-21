    public static void fixMapFile(String filestr) throws IOException {
        File mapfile = new File(filestr);
        File tempmapfile = new File(filestr + "temp");
        if (!mapfile.isAbsolute()) throw new IllegalArgumentException(filestr + " is not a valid file");
        tempmapfile.createNewFile();
        Scanner scan = new Scanner(mapfile);
        PrintStream ps = new PrintStream(tempmapfile);
        ps.println(scan.nextLine());
        String line;
        String parts[];
        while (scan.hasNextLine()) {
            line = scan.nextLine();
            parts = line.split(";");
            String newent;
            for (int i = 0; i < parts.length; i++) {
                newent = getNewEntryString(parts[i]);
                ps.print(newent + ";");
            }
            ps.append("\n");
        }
        mapfile.delete();
        tempmapfile.renameTo(mapfile);
    }
