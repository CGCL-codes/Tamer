    private boolean loadTile(String url, int x, int y) {
        InputStream in;
        FileOutputStream out;
        byte data[];
        int len, rLen = 0;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            request.setHeader("User-Agent", "LocDemo WLocate demo application");
            HttpResponse response = client.execute(request);
            in = response.getEntity().getContent();
            len = (int) response.getEntity().getContentLength();
            if (len <= 0) return false;
            data = new byte[len];
            while (rLen < len) {
                rLen += in.read(data, rLen, len - rLen);
            }
            out = getContext().openFileOutput("tile_" + m_zoom + "_" + (m_tileX + x - xOffs) + "_" + (m_tileY + y - yOffs) + ".png", Context.MODE_PRIVATE);
            out.write(data);
            out.close();
            locTile[x][y] = BitmapFactory.decodeByteArray(data, 0, rLen);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
