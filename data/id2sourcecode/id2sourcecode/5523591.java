    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        try {
            AssetManager am = ctx.getAssets();
            String path = uri.getPath();
            InputStream is = am.open("evangelioCapitulos" + path);
            File dir = new File("data/data/net.mym.evangelio.br/files/");
            dir.mkdirs();
            File file = new File("data/data/net.mym.evangelio.br/files/temporalPdf.pdf");
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            is.close();
            parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return parcel;
    }
