    private void replace(ID id, String rep, File src, File dest) {
        RandomAccessFile fis = null;
        PrintWriter pw = null;
        try {
            if (!dest.exists()) dest.createNewFile();
            fis = new RandomAccessFile(src, "r");
            pw = new PrintWriter(dest);
            int location = 0;
            char next;
            fis.seek(location);
            while (location < fis.length()) {
                String currMatch = id.atLocation(location);
                if (currMatch != null) {
                    location += currMatch.length();
                    fis.seek(location);
                    pw.print(rep);
                } else {
                    next = (char) fis.read();
                    location++;
                    pw.print(next);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("A file specified by the replace method was not found.");
            System.out.println(ex.getMessage());
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Error attempting to read or write to specified file.");
            System.out.println(ex.getMessage());
            System.exit(0);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
            }
            if (pw != null) pw.close();
        }
    }
