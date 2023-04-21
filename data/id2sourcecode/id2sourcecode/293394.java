    public File start(Indi indi) {
        File file = getFileFromUser(translate("output.file"), Action2.TXT_OK, true);
        if (file == null) return null;
        try {
            writer = getWriter(new FileOutputStream(file));
            Reader in = new InputStreamReader(getClass().getResourceAsStream("ps-fan.ps"));
            int c;
            writer.println("%!PS-Adobe-3.0");
            writer.println("%%Creator: genj 1.0");
            writer.println("%%CreationDate: ");
            writer.println("%%PageOrder: Ascend");
            writer.println("%%Orientation: Landscape");
            writer.println("%%EndComments");
            writer.println("/maxlevel " + genPerPage + " def");
            writer.println("/color " + (useColors ? "true" : "false") + " def");
            while ((c = in.read()) != -1) writer.write(c);
            in.close();
        } catch (IOException ioe) {
            System.err.println("IO Exception!");
            ioe.printStackTrace();
        }
        indiList.add(indi);
        indiList.add(new Integer(1));
        pageNo = 1;
        while (!indiList.isEmpty()) {
            Indi indiIterator = (Indi) (indiList.removeFirst());
            Integer genIndex = (Integer) (indiList.removeFirst());
            if (genIndex != null) {
                writer.println("gsave");
                pedigree(1, genIndex.intValue(), 1, 1, indiIterator);
                writer.println("showpage");
                pageNo++;
                writer.println("grestore");
            }
        }
        writer.flush();
        writer.close();
        return file;
    }
