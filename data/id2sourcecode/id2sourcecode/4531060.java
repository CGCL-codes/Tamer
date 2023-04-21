    private void startDownload(final File file) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        });
        try {
            URL url = new URL("http://www.pdb.org/pdb/files/" + file.getName());
            InputStream stream = url.openStream();
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = stream.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
