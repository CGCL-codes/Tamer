    public void postDocument() {
        String formatid = dtm1.getValueAt(tableformat.getSelectedRow(), 0).toString();
        String libId = newgen.presentation.NewGenMain.getAppletInstance().getLibraryID();
        java.util.Vector vec = new java.util.Vector();
        vec.add(libId);
        vec.add(formatid);
        Object[] obj = new Object[3];
        obj[0] = "UPLOAD";
        obj[1] = "FORMLETTER";
        try {
            java.io.File fOO = new java.io.File(ooFile);
            java.io.File fHTML = new java.io.File(htmlFile);
            java.nio.channels.FileChannel fcOO = (new java.io.FileInputStream(fOO)).getChannel();
            java.nio.channels.FileChannel fcHTML = (new java.io.FileInputStream(fHTML)).getChannel();
            int fileLengthOO = (int) fcOO.size();
            int fileLengthHTML = (int) fcHTML.size();
            java.nio.MappedByteBuffer bbOO = fcOO.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLengthOO);
            java.nio.MappedByteBuffer bbHTML = fcHTML.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLengthHTML);
            byte[] byx1 = new byte[bbOO.capacity()];
            byte[] byx2 = new byte[bbHTML.capacity()];
            bbOO.get(byx1);
            vec.add(byx1);
            bbHTML.get(byx2);
            vec.add(byx2);
            obj[2] = vec;
            String xmlStr = "";
            xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendObjectRequest("FileUploadDownloadServlet", obj).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
