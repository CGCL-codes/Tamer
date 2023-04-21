    public static void launchhelper(final PlayURL pu, final Activity a, final Dialog previousdialog) {
        String TAG = "launchhelper";
        Log.d(TAG, "START");
        Uri uri = null;
        Intent i = null;
        if (pu == null) {
            Log.d(TAG, "No data received");
            Toast.makeText(a, "Unable to grab audio. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        String playthis = pu.getURL();
        Log.d(TAG, "Only one url: " + playthis);
        i = new Intent(Intent.ACTION_VIEW);
        uri = Uri.parse(playthis);
        URL url;
        URLConnection urlConn = null;
        try {
            url = new URL(playthis);
            urlConn = (HttpURLConnection) url.openConnection();
            String ctype = urlConn.getContentType();
            Log.d(TAG, "Content Type: " + ctype);
            if (ctype == null) {
                ctype = "";
            }
            if (ctype.contains(StreamingMediaPlayer.AUDIO_MPEG) || ctype.contains(PlayListTab.RSS_MIME) || ctype.contains(PlayListTab.XML_MIME) || ctype.equals("")) {
                i = new Intent(MyNPR.tPLAY);
                i.putExtra(PlayListTab.URL, pu.getURL());
                i.putExtra(PlayListTab.STATION, pu.getStation());
                i.putExtra(PlayListTab.LOGO, pu.getLogo());
                if (ctype.contains(PlayListTab.RSS_MIME) || ctype.contains(PlayListTab.XML_MIME) || pu.isRSS()) {
                    Log.d(TAG, "RSS content");
                    i.putExtra(PlayList.SPLITTERRSS, true);
                    i.putExtra(PlayList.SPLITTERRSSTITLE, pu.getTitle());
                }
                Uri u = i.getData();
                if (u == null) {
                    Log.e(TAG, "uri null");
                } else {
                    Log.v(TAG, "uri okay: " + u.toString());
                }
                Log.v(TAG, "mime type: " + i.getType());
                Log.d(TAG, "Launch Playlist Tab");
                if (previousdialog != null) {
                    previousdialog.dismiss();
                }
                MyNPR parent = (MyNPR) a.getParent();
                Log.d(TAG, "Switch to playlist tab");
                parent.tabHost.setCurrentTabByTag(MyNPR.tPLAY);
                Log.d(TAG, "Broadcast playlist intent");
                a.sendBroadcast(i);
            } else {
                uri = Uri.parse(playthis);
                i = new Intent(Intent.ACTION_VIEW, uri, a, com.webeclubbin.mynpr.HTMLviewer.class);
                Log.d(TAG, "Launch HTML viewer");
                a.startActivity(i);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Could not connect to " + playthis);
        }
    }
