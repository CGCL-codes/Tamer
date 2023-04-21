    public void writeToHtml(String filename) {
        if (filename.endsWith(".html") | filename.endsWith(".HTML")) {
        } else {
            filename = filename + ".html";
        }
        File file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
        try {
            BufferedWriter pen = new BufferedWriter(new FileWriter(filename));
            pen.write("<html><title>" + title + "</title>");
            pen.newLine();
            pen.write("<h1><b>" + title + "</b></h1>");
            pen.newLine();
            pen.write("<h2>Table Of Contents:</h2><ul>");
            pen.newLine();
            for (int k = 0; k < rings.size(); k++) {
                pen.write("<li><i><a href=\"#");
                pen.write(readNoteTitle(k));
                pen.write("\">");
                pen.write(readNoteTitle(k));
                pen.write("</a></i></li>");
                pen.newLine();
            }
            pen.write("</ul><hr>");
            pen.newLine();
            for (int k = 0; k < rings.size(); k++) {
                pen.write("<h3><b>");
                pen.write("<a name=\"");
                pen.write(readNoteTitle(k));
                pen.write("\">");
                pen.write(readNoteTitle(k) + "</a>");
                pen.write("</b></h3>");
                pen.newLine();
                pen.write("<a>");
                String[] tempText = { new String(readNoteBody(k)) };
                tempText = tempText[0].split("\n");
                for (int p = 0; p < tempText.length; p++) {
                    pen.write(tempText[p] + "<br>");
                }
                pen.write("</a>");
                pen.newLine();
            }
            pen.newLine();
            pen.write("<br><hr><a>This was created using Scribe, a free crutch editor.</a></html>");
            pen.flush();
            pen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
