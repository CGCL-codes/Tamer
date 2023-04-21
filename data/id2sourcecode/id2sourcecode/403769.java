    public void start(Indi indi) {
        File file = getFileFromUser(translate("output.file"), Action2.TXT_OK, true);
        if (file == null) return;
        try {
            out = getWriter(new FileOutputStream(file));
            Reader in = new InputStreamReader(getClass().getResourceAsStream("ps-fan.ps"));
            int c;
            out.println("%!PS-Adobe-3.0");
            out.println("%%Creator: genj 1.0");
            out.println("%%CreationDate: ");
            out.println("%%PageOrder: Ascend");
            out.println("%%Orientation: Landscape");
            out.println("%%EndComments");
            out.println("/maxlevel " + genPerPage + " def");
            out.println("/color " + (useColors ? "true" : "false") + " def");
            while ((c = in.read()) != -1) out.write(c);
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
                out.println("gsave");
                pedigree(1, genIndex.intValue(), 1, 1, indiIterator);
                out.println("showpage");
                pageNo++;
                out.println("grestore");
            }
        }
        out.flush();
        out.close();
        showFileToUser(file);
    }
