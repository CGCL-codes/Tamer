    public static void launchhelper(String[] s, final Activity a, final Dialog previousdialog, final String station, final String logo) {
        String TAG = "launchhelper";
        Log.d(TAG, "START");
        Uri uri = null;
        Intent i = null;
        if (s == null) {
            Log.d(TAG, "No data received");
            Toast.makeText(a, "Unable to grab audio. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        if (s.length == 1) {
            String playthis = s[0];
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
                if (ctype.contains(StreamingMediaPlayer.AUDIO_MPEG) || ctype.equals("")) {
                    i = new Intent(MyNPR.tPLAY);
                    i.putExtra(PlayListTab.STATION, station);
                    i.putExtra(PlayListTab.LOGO, logo);
                    i.putExtra(PlayListTab.URL, uri.toString());
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
        } else {
            final Dialog d = new Dialog(a);
            d.setContentView(com.webeclubbin.mynpr.R.layout.urlpopup);
            d.setTitle("Multiple Audio links found.");
            ListView lv2 = (ListView) d.findViewById(com.webeclubbin.mynpr.R.id.urllist);
            lv2.setAdapter(new ArrayAdapter<String>(a, com.webeclubbin.mynpr.R.layout.urllist, s));
            lv2.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Uri uri = null;
                    Intent i = null;
                    String playthis;
                    String TAG = "Intent / Open URL";
                    TextView t = (TextView) v;
                    String loc = t.getText().toString();
                    String[] r = null;
                    if (loc.toLowerCase().endsWith(PLS)) {
                        r = parsePLS(loc, a);
                    } else if (loc.toLowerCase().endsWith(M3U)) {
                        r = parseM3U(loc, a);
                    } else {
                        r = new String[] { loc };
                    }
                    if (r == null) {
                        Log.d(TAG, "No data returned");
                        Toast.makeText(a, "No Audio Found inside Station's Playlist", Toast.LENGTH_LONG).show();
                    } else if ((r.length == 1) && (!r[0].toLowerCase().endsWith(PLS)) && (!r[0].toLowerCase().endsWith(M3U))) {
                        Log.d(TAG, "Found One");
                        launchhelper(r, a, d, station, logo);
                    } else {
                        Log.d(TAG, "Found Several or a list: ");
                        launchhelper(r, a, d, station, logo);
                    }
                }
            });
            if (previousdialog != null) {
                Log.d(TAG, "Show Dialog and dismiss old one");
                previousdialog.dismiss();
            }
            d.show();
        }
    }
