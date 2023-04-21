    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Object[] object = new Object[3];
            object[0] = "UPLOAD";
            object[1] = "CatalogueRecords";
            java.util.Vector vector = new java.util.Vector();
            java.io.File zipFile = new java.io.File(unicodeTextField2.getText());
            System.out.println("path is" + zipFile.getAbsolutePath());
            System.out.println("file exists" + zipFile.exists());
            System.out.println("file length" + zipFile.length());
            java.nio.channels.FileChannel fileChannel = null;
            fileChannel = (new java.io.FileInputStream(zipFile)).getChannel();
            int fileLength = (int) fileChannel.size();
            System.out.println("fileLength : " + fileLength);
            java.nio.MappedByteBuffer mappedByteBuffer = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
            byte[] byteArray = new byte[mappedByteBuffer.capacity()];
            System.out.println(byteArray.length);
            System.out.println(mappedByteBuffer.hasArray());
            fileChannel.close();
            mappedByteBuffer.get(byteArray);
            vector.addElement(unicodeTextField1.getText());
            vector.addElement(byteArray);
            object[2] = vector;
            Object retObject = newgen.presentation.component.ServletConnector.getInstance().sendObjectRequestToSpecifiedServer(unicodeTextField1.getText(), "FileUploadDownloadServlet", object);
            vector = (java.util.Vector) retObject;
            System.out.println("Return value is: " + vector.elementAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
