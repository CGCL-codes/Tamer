    public static String loadResToString(int resId, Activity ctx) {
        try {
            InputStream is = ctx.getResources().openRawResource(resId);
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                int read = is.read(buffer);
                if (read == -1) {
                    break;
                }
                baos.write(buffer, 0, read);
            }
            baos.close();
            is.close();
            String data = baos.toString();
            Log.i(Global.TAG, "ResourceUtils loaded resource to string:" + data);
            return data;
        } catch (Exception e) {
            Log.e(Global.TAG, "ResourceUtils failed to load resource to string", e);
            return null;
        }
    }
