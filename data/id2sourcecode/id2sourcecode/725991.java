    public void exportList(String filename) {
        if (filename.endsWith(".list") | filename.endsWith(".LIST")) {
        } else {
            filename = filename + ".list";
        }
        File file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
        try {
            BufferedWriter pen = new BufferedWriter(new FileWriter(filename));
            for (int k = 0; k < rings.size(); k++) {
                pen.write(readNoteTitle(k));
                pen.newLine();
            }
            pen.flush();
            pen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
