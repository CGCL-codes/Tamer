    private void openLocation(double longitude, double latitude) {
        try {
            URL url = new URL(String.format(QUERY_URI_TEMPLATE, latitude, longitude));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copy(url.openStream(), baos);
            JSONObject object = new JSONObject(new String(baos.toByteArray()));
            JSONObject center = object.getJSONObject("searchSetup").getJSONObject("center");
            long x = center.getLong("x");
            long y = center.getLong("y");
            String targetUri = String.format(MAP_URI_TEMPLATE, x, y, latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, "net.krecan.mapky");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("mapky", e.getMessage());
        }
    }
