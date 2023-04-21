    public void write() {
        int i;
        try {
            OutputStream os = new FileOutputStream(filename);
            ZipOutputStream zos = new ZipOutputStream(os);
            OutputStreamWriter out;
            if (compressed) {
                zos.putNextEntry(new ZipEntry("QuizCards XML"));
                out = new OutputStreamWriter(zos);
                XML.startFile(out);
            } else {
                out = new OutputStreamWriter(os, "UTF-8");
                XML.startFile(out);
            }
            XML.startElement("qcards:stack", "version", String.valueOf(QuizCards.version));
            XML.startElement("properties");
            XML.writeElement("title", title);
            XML.writeElement("left_name", lName);
            XML.writeElement("right_name", rName);
            XML.writeElement("left_font", lFontName);
            XML.writeElement("right_font", rFontName);
            XML.writeElement("left_font_size", Integer.toString(lFontSize));
            XML.writeElement("right_font_size", Integer.toString(rFontSize));
            XML.writeElement("left_sound", String.valueOf(lSoundFlag));
            XML.writeElement("right_sound", String.valueOf(rSoundFlag));
            XML.finishElement();
            sets.write();
            XML.startElement("cards");
            for (i = 0; i < cards.size(); i++) ((Card) cards.elementAt(i)).write(i, sets);
            XML.finishElement();
            XML.finishElement();
            XML.finishFile();
            out.flush();
            if (compressed) {
                DataOutputStream dos = new DataOutputStream(zos);
                zos.closeEntry();
                for (i = 0; i < cards.size(); i++) {
                    ((Card) cards.elementAt(i)).writeFiles(zos, dos);
                }
                dos.flush();
                zos.flush();
                zos.finish();
            }
            os.flush();
            dirty = false;
        } catch (IOException e) {
            System.out.println(e);
        }
    }
