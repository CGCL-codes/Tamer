    private int copyRawFileToRepo(String filePath, String uri, String infosourceUri, String mimeType) throws FileNotFoundException, Exception, IOException {
        InfoSource infoSource = infoSourceDAO.getByUri(infosourceUri);
        Reader ir = new FileReader(filePath);
        DocumentOutputStream dos = repository.getDocumentOutputStream(infoSource.getId(), uri, mimeType);
        OutputStreamWriter w = new OutputStreamWriter(dos.getOutputStream());
        char[] buff = new char[1000];
        int read = 0;
        while ((read = ir.read(buff)) > 0) {
            w.write(buff, 0, read);
        }
        w.flush();
        w.close();
        return dos.getId();
    }
