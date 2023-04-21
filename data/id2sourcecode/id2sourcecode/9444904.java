    public boolean writeToFile(Document doc, String fileName, boolean saveAsLocalFile) {
        OutputStreamWriter outputStreamWriter = null;
        FileOutputStream fileStream = null;
        URL url = null;
        BufferedOutputStream outputStream = null;
        boolean saveRemoteFile = !saveAsLocalFile;
        try {
            if (saveRemoteFile) {
                url = new URL(DefaultServerSettings.getFTPURL());
                revision = Long.parseLong(DefaultServerSettings.getResponce());
                URLConnection urlc = url.openConnection();
                outputStream = new BufferedOutputStream(urlc.getOutputStream());
            } else {
                fileStream = new FileOutputStream(fileName);
                outputStream = new BufferedOutputStream(fileStream);
            }
            XMLOutputter XMLOut = new XMLOutputter();
            XMLOut.setFormat(XMLOut.getFormat().setExpandEmptyElements(true));
            if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).equalsIgnoreCase(".aopz")) {
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                ZipEntry entry = new ZipEntry("pathways.aop");
                zipOutputStream.putNextEntry(entry);
                XMLOut.output(doc, zipOutputStream);
                zipOutputStream.close();
                outputStream.close();
                System.out.println("entry.size:" + entry.getSize());
                if (!saveRemoteFile) fileStream.close();
            } else {
                if (saveRemoteFile) outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8"); else outputStreamWriter = new OutputStreamWriter(fileStream, "UTF-8");
                XMLOut.output(doc, outputStreamWriter);
                outputStreamWriter.close();
            }
            return true;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
