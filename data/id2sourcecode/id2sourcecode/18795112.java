    private String createDefaultIcon(long channelId) {
        String icoName = "channel" + channelId + ".ico";
        FileOutputStream ico = null;
        InputStream def = null;
        String name = null;
        try {
            ico = getContext().openFileOutput(icoName, Context.MODE_PRIVATE);
            def = getContext().getResources().openRawResource(R.drawable.feedicon);
            byte[] buf = new byte[1024];
            int n;
            while ((n = def.read(buf)) != -1) ico.write(buf, 0, n);
            name = getIconPath(channelId);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        } finally {
            try {
                if (ico != null) ico.close();
                if (def != null) def.close();
            } catch (Exception e) {
                return null;
            }
        }
        return name;
    }
