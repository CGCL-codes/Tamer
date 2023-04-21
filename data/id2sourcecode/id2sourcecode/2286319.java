    private void uploadAttachFileToServer(Part p, String filePathName) throws MessagingException, IOException {
        File file = new File(filePathName);
        if (file.exists()) throw new IOException("�ļ��Ѵ���");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        InputStream is = p.getInputStream();
        int c;
        while ((c = is.read()) != -1) os.write(c);
        os.close();
    }
