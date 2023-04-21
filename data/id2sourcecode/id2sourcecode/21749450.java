    @Override
    protected void onStart() {
        Log.v(TAG, "Starting\n");
        super.onStart();
        on = true;
        if (dont_restore) dont_restore = false; else {
            Utils.changeLanguage(this);
            Intent intent = getIntent();
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            Panels.State s = panels.new State();
            s.restore(prefs);
            String action = intent.getAction();
            Log.i(TAG, "Action: " + action);
            int use_panel = -1;
            Uri uri = intent.getData();
            if (uri != null && !viewActProcessed && Intent.ACTION_VIEW.equals(action)) {
                Log.v(TAG, "Intent URI: " + uri);
                Credentials crd = null;
                try {
                    crd = (Credentials) intent.getParcelableExtra(Credentials.KEY);
                } catch (Throwable e) {
                    Log.e(TAG, "on extracting credentials from an intent", e);
                }
                String file_name = null;
                String type = intent.getType();
                if ("application/x-zip-compressed".equals(type) || "application/zip".equals(type)) uri = uri.buildUpon().scheme("zip").build(); else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        if (is != null) {
                            File dwf = new File(Panels.DEFAULT_LOC, "download");
                            if (!dwf.exists()) dwf.mkdirs();
                            String fn = uri.getLastPathSegment();
                            File f = null;
                            for (int i = 0; i < 99; i++) {
                                file_name = i == 0 ? fn : fn + "_" + i;
                                f = new File(dwf, file_name);
                                if (f.exists()) continue;
                                if (f.createNewFile()) break;
                                f = null;
                            }
                            if (f != null) {
                                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f), 8192);
                                byte[] buf = new byte[4096];
                                int n;
                                while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
                                bos.close();
                                is.close();
                                uri = Uri.fromFile(dwf);
                                showMessage(getString(R.string.copied_f, f.toString()));
                            } else showError(getString(R.string.not_accs, fn));
                        }
                    } catch (Exception e) {
                        showError(getString(R.string.not_accs, ""));
                    }
                }
                use_panel = Panels.LEFT;
                panels.Navigate(use_panel, uri, crd, file_name);
                viewActProcessed = true;
            }
            panels.setState(s, use_panel);
            final String FT = "first_time";
            if (!viewActProcessed && prefs.getBoolean(FT, true)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(FT, false);
                editor.commit();
                showInfo(getString(R.string.keys_text));
            }
            if (use_panel >= 0 && viewActProcessed) panels.setPanelCurrent(use_panel);
            if (Intent.ACTION_SEARCH_LONG_PRESS.equals(action)) {
                showSearchDialog();
                return;
            }
        }
    }
