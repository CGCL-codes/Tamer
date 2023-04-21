    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        try {
            AssetManager am = ctx.getAssets();
            InputStream is = am.open("genplano.pdf");
            File dir = new File("data/data/net.mym.bcnmetro/files/");
            dir.mkdirs();
            File file = new File("data/data/net.mym.bcnmetro/files/genplano.pdf");
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
