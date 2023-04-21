    public void writeToTxt(String filename) {
        if (filename.endsWith(".txt") | filename.endsWith(".TXT")) {
        } else {
            filename = filename + ".txt";
        }
        File file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
        try {
            BufferedWriter pen = new BufferedWriter(new FileWriter(filename));
            pen.write(title);
            pen.newLine();
            pen.newLine();
            for (int k = 0; k < rings.size(); k++) {
                pen.write(readNoteTitle(k));
                pen.newLine();
                pen.write(readNoteBody(k));
                pen.newLine();
                pen.newLine();
            }
            pen.flush();
            pen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
