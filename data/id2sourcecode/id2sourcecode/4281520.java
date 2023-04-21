    public static void main(String[] args) throws IOException {
        new File(output).mkdir();
        IntegratedDocPlaceMakerAndTimexIterator iterator = new IntegratedDocPlaceMakerAndTimexIterator(documentPath, timexesPath, placemakerPath);
        String fileId = "nyt_eng_200201";
        String filePath = output + "\\" + fileId + ".sentences.xml";
        FileWriter nowWriter = new FileWriter(filePath, false);
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            IntegratedDocPlaceMakerAndTimexIterator.DocumentWithPlacesAndTimexes document = iterator.next();
            if (!document.getD().getDId().substring(0, 14).toLowerCase().equals(fileId)) {
                nowWriter.flush();
                nowWriter.close();
                FileInputStream fileInputStream = new FileInputStream(filePath);
                File toDelete = new File(filePath);
                String fileZipPath = output + "\\" + fileId + ".sentences.zip";
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(fileZipPath)));
                zipOutputStream.putNextEntry(new ZipEntry(fileId + ".sentences.xml"));
                fileId = document.getD().getDId().substring(0, 14).toLowerCase();
                filePath = output + "\\" + fileId + ".sentences.xml";
                StreamsUtils.inputStream2OutputStream(fileInputStream, zipOutputStream);
                nowWriter = new FileWriter(output + "\\" + fileId + ".sentences.xml", false);
                toDelete.delete();
                toDelete.deleteOnExit();
            }
            if (count > 100) {
                count = 0;
                nowWriter.flush();
            }
            writeSentences(nowWriter, document);
        }
        nowWriter.flush();
        nowWriter.close();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        File toDelete = new File(filePath);
        String fileZipPath = output + "\\" + fileId + ".sentences.zip";
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(fileZipPath)));
        zipOutputStream.putNextEntry(new ZipEntry(fileId + ".sentences.xml"));
        StreamsUtils.inputStream2OutputStream(fileInputStream, zipOutputStream);
        toDelete.delete();
        toDelete.deleteOnExit();
    }
