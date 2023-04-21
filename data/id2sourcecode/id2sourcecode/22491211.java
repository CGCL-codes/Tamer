    public void writeChapterOutput(ZipOutputStream outputStream, String cleanSGML) {
        boolean isCompressedOutput = false;
        if (outputStream != null) {
            isCompressedOutput = true;
        }
        if (isCompressedOutput) {
            if (witsInstance.getOutputType().equals("solbook")) {
                SolChapterWriter cWriter = new SolChapterWriter(cleanSGML, props);
                ZipEntry entry = new ZipEntry(chapterPath.getName());
                try {
                    outputStream.putNextEntry(entry);
                    outputStream.write(cWriter.getChapterBody().getBytes());
                    outputStream.closeEntry();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (witsInstance.getOutputType().equals("docbook")) {
                DocChapterWriter cWriter = new DocChapterWriter(cleanSGML, props);
                ZipEntry entry = new ZipEntry(chapterPath.getName());
                try {
                    outputStream.putNextEntry(entry);
                    outputStream.write(cWriter.getChapterBody().getBytes());
                    outputStream.closeEntry();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            try {
                FileWriter fw = new FileWriter(chapterPath);
                if (witsInstance.getOutputType().equals("solbook")) {
                    SolChapterWriter cWriter = new SolChapterWriter(cleanSGML, props);
                    fw.write(cWriter.getChapterBody());
                    fw.flush();
                    fw.close();
                }
                if (witsInstance.getOutputType().equals("docbook")) {
                    DocChapterWriter cWriter = new DocChapterWriter(cleanSGML, props);
                    fw.write(cWriter.getChapterBody());
                    fw.flush();
                    fw.close();
                }
            } catch (IOException e) {
                System.out.println("Error while writing to: " + chapterPath + ". PLease check the path/permission.");
            }
        }
    }
