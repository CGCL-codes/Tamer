    public static ArrayList<Card> importCards(String zipFileName) {
        ArrayList<Card> cards = new ArrayList<Card>();
        try {
            ByteArrayOutputStream tempByteXML = new ByteArrayOutputStream();
            ByteArrayOutputStream tempBytePicZip = new ByteArrayOutputStream();
            FileInputStream zipFileIn = new FileInputStream(zipFileName);
            System.out.println(" availt " + zipFileIn.available());
            ZipInputStream inZip = new ZipInputStream(zipFileIn);
            ZipEntry entry;
            byte[] buf = new byte[1024];
            int len;
            while ((entry = inZip.getNextEntry()) != null) {
                String fileName = entry.getName();
                System.out.println("filename " + fileName);
                if (fileName.equals(XML_FILE_NAME)) {
                    while ((len = inZip.read(buf)) > 0) {
                        tempByteXML.write(buf, 0, len);
                    }
                }
                if (fileName.equals(ZIP_PIC_FILE_NAME)) {
                    while ((len = inZip.read(buf)) > 0) {
                        tempBytePicZip.write(buf, 0, len);
                    }
                }
            }
            System.out.println("entry size :" + tempByteXML.toByteArray().length);
            String xml = new String(tempByteXML.toByteArray(), "UTF-8");
            cards = convertXMLToCards(xml);
            inZip.close();
            zipFileIn.close();
            tempByteXML.close();
            ByteArrayInputStream picZipInput = new ByteArrayInputStream(tempBytePicZip.toByteArray());
            tempBytePicZip.close();
            ZipInputStream inZipPic = new ZipInputStream(picZipInput);
            File imageFolder = new File(IMAGE_FOLDER);
            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }
            while ((entry = inZipPic.getNextEntry()) != null) {
                String fileName = entry.getName();
                System.out.println("filename " + fileName);
                String newFileName = IMAGE_FOLDER + File.separator + fileName;
                FileOutputStream newFileOut = new FileOutputStream(newFileName);
                while ((len = inZipPic.read(buf)) > 0) {
                    newFileOut.write(buf, 0, len);
                }
                newFileOut.close();
                for (Iterator<Card> it = cards.iterator(); it.hasNext(); ) {
                    Card card = it.next();
                    if (card.getImgFront().equals(fileName)) {
                        card.setImgFront(newFileName);
                    }
                    if (card.getImgBack().equals(fileName)) {
                        card.setImgBack(newFileName);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cards;
    }
