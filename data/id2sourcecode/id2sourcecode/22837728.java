    public java.util.Vector download(Object[] obj) {
        java.util.Vector retvec = new java.util.Vector(1, 1);
        String purpose = obj[1].toString();
        System.out.println("purpose=" + purpose);
        if (purpose.equals("PATRONPHOTO")) {
            try {
                java.util.ArrayList alParameters = (java.util.ArrayList) obj[2];
                System.out.println("here 1");
                String patronId = alParameters.get(0).toString();
                String libraryId = alParameters.get(1).toString();
                System.out.println("here2");
                String fileSeperator = System.getProperties().get("file.separator").toString();
                java.io.File patpho = new java.io.File(newGenLibRoot.getRoot() + "/PatronPhotos/" + "LIB_" + libraryId + "/" + "PAT_" + patronId + ".jpg");
                System.out.println("patronId : " + patronId);
                retvec.addElement("PAT_" + patronId + ".jpg");
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(patpho)).getChannel();
                int fileLength = (int) fc.size();
                System.out.println("fileLength : " + fileLength);
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byte[] byx = new byte[bb.capacity()];
                System.out.println(byx.length);
                System.out.println(bb.hasArray());
                fc.close();
                bb.get(byx);
                System.out.println(byx.length);
                retvec.addElement(byx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (purpose.equals("CATALOGUEATTACHMENTS")) {
            try {
                java.util.ArrayList alParameters = (java.util.ArrayList) obj[2];
                String catrecid = alParameters.get(0).toString();
                String libid = alParameters.get(1).toString();
                String filename = alParameters.get(2).toString();
                java.io.File ff = new java.io.File(newGenLibRoot.getAttachmentsPath() + "/CatalogueRecords/CAT_" + catrecid + "_" + libid + "/" + filename);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(ff)).getChannel();
                int fileLength = (int) fc.size();
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byte[] byx = new byte[bb.capacity()];
                fc.close();
                bb.get(byx);
                retvec.addElement(filename);
                retvec.addElement(byx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (purpose.equals("MAPPHOTO")) {
            byte[] byx = new byte[100];
            try {
                String libraryId = ((String[]) obj[2])[0];
                System.out.println("library id in location hancler=" + libraryId);
                String filename = ((String[]) obj[2])[1];
                System.out.println("========this is in map download=========");
                String filepath = newGenLibRoot.getRoot() + "/Maps";
                filepath += "/LIB_" + libraryId + "/" + filename;
                System.out.println("in location handler file path=" + filepath);
                java.io.File actualfile = new java.io.File(filepath);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(actualfile)).getChannel();
                int fileLength = (int) fc.size();
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byx = new byte[bb.capacity()];
                bb.get(byx);
                retvec.addElement(byx);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return retvec;
    }
