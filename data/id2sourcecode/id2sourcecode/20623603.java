    public void onFileOpen() {
        if (getApp().isStandAlone) {
            super.onFileOpen();
            return;
        }
        String strURL = null;
        try {
            strURL = getApp().getCodeBase().toString() + "example.tra";
            System.out.println(strURL);
            URL urlFile = new URL(strURL);
            InputStream isNew = urlFile.openStream();
            DataInputStream disNew = new DataInputStream(isNew);
            openFileShared("Example", disNew);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(strURL);
        }
    }
