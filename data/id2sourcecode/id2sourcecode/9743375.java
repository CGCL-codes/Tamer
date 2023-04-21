    @Test
    public void testDownload() throws IOException {
        testLogin();
        Download download = new AppstoreAPIStub.Download();
        download.setId(4);
        AppstoreAPIStub.DownloadE downloadImpl = new AppstoreAPIStub.DownloadE();
        downloadImpl.setDownload(download);
        DataHandler data = stub.download(downloadImpl).getDownloadResponse().get_return();
        InputStream inputStream = data.getInputStream();
        File f = new File("/home/jhahn/Desktop/htmlTest.jhp");
        OutputStream out = new FileOutputStream(f);
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
        out.close();
        inputStream.close();
        assert f.length() > 0;
    }
