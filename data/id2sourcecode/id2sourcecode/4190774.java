    public static String getRemoteHtml(String urlpath, final Activity activity) {
        try {
            URL url = new URL(urlpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                byte[] data = HttpUtil.getByte(conn.getInputStream());
                return new String(data, "utf-8");
            }
        } catch (Exception e) {
            AlertDialog.Builder b = new AlertDialog.Builder(activity);
            b.setTitle("Alert");
            b.setMessage("对不起，连接已超时，请稍后重试");
            b.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            b.setCancelable(false);
            b.create();
            b.show();
            return "";
        }
        return "";
    }
