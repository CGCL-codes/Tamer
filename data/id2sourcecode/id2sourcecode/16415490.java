    public static int duplicateFile(String writeFilenameP, String readFilenameP) {
        BufferedWriter writeFile = createFile(writeFilenameP);
        Scanner readFile = openFile(readFilenameP);
        int countlines = 0;
        while (readFile.hasNextLine()) {
            String textFileLine = readFile.nextLine() + '\n';
            try {
                writeFile.write(textFileLine);
            } catch (IOException e) {
                System.out.println();
                System.out.println("**********I/O ERROR while trying to write a line in the file: " + writeFilenameP + "**********");
            }
            countlines++;
        }
        readFile.close();
        try {
            writeFile.close();
        } catch (IOException e) {
            System.out.println();
            System.out.println("**********I/O ERROR while trying to close the file: " + writeFilenameP + "**********");
        }
        return countlines;
    }
