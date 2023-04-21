    public void updateLayout() {
        FullUpdateInfo mAvailableUpdates = null;
        try {
            mAvailableUpdates = State.loadState(this, showDebugOutput);
        } catch (IOException e) {
            Log.e(TAG, "Unable to restore activity status", e);
        }
        View roms = findViewById(R.id.rom_layout);
        TextView experimentalBuildsRomtv = (TextView) roms.findViewById(R.id.experimental_rom_updates_textview);
        TextView showDowngradesRomtv = (TextView) roms.findViewById(R.id.show_rom_downgrades_textview);
        TextView lastRomUpdateChecktv = (TextView) roms.findViewById(R.id.last_rom_update_check);
        Button selectUploadButton = (Button) roms.findViewById(R.id.download_update_button);
        TextView DownloadText = (TextView) roms.findViewById(R.id.available_updates_text);
        LinearLayout stableExperimentalInfoUpdates = (LinearLayout) roms.findViewById(R.id.stable_experimental_description_container_updates);
        Button changelogButton = (Button) roms.findViewById(R.id.show_changelog_button);
        Button CheckNowUpdateChooserUpdates = (Button) roms.findViewById(R.id.check_now_button_update_chooser_updates);
        TextView CheckNowUpdateChooserTextUpdates = (TextView) roms.findViewById(R.id.check_now_update_chooser_text_updates);
        View existing = findViewById(R.id.existing_layout);
        TextView mdownloadedUpdateText = (TextView) existing.findViewById(R.id.downloaded_update_found);
        Button mdeleteOldUpdatesButton = (Button) existing.findViewById(R.id.delete_updates_button);
        Button mapplyUpdateButton = (Button) existing.findViewById(R.id.apply_update_button);
        TextView mNoExistingUpdatesFound = (TextView) existing.findViewById(R.id.no_existing_updates_found_textview);
        View themes = null;
        TextView showDowngradesThemetv = null;
        TextView experimentalBuildsThemetv = null;
        TextView lastThemeUpdateChecktv = null;
        Button btnDownloadTheme = null;
        TextView tvThemeDownloadText = null;
        LinearLayout stableExperimentalInfoThemes = null;
        Button btnThemechangelogButton = null;
        Button btnThemeScreenshotButton = null;
        TextView tvNoThemeUpdateServer = null;
        Button CheckNowUpdateChooserThemes = null;
        TextView CheckNowUpdateChooserTextThemes = null;
        if (Customization.Screenshotsupport) {
            themes = findViewById(R.id.themes_layout);
            showDowngradesThemetv = (TextView) themes.findViewById(R.id.show_theme_downgrades_textview);
            experimentalBuildsThemetv = (TextView) themes.findViewById(R.id.experimental_theme_updates_textview);
            lastThemeUpdateChecktv = (TextView) themes.findViewById(R.id.last_theme_update_check);
            btnDownloadTheme = (Button) themes.findViewById(R.id.download_theme_button);
            tvThemeDownloadText = (TextView) themes.findViewById(R.id.available_themes_text);
            stableExperimentalInfoThemes = (LinearLayout) themes.findViewById(R.id.stable_experimental_description_container_themes);
            btnThemechangelogButton = (Button) themes.findViewById(R.id.show_theme_changelog_button);
            btnThemeScreenshotButton = (Button) themes.findViewById(R.id.theme_screenshots_button);
            tvNoThemeUpdateServer = (TextView) themes.findViewById(R.id.no_theme_update_server_configured);
            CheckNowUpdateChooserThemes = (Button) themes.findViewById(R.id.check_now_button_update_chooser_themes);
            CheckNowUpdateChooserTextThemes = (TextView) themes.findViewById(R.id.check_now_update_chooser_text_themes);
        }
        List<String> existingFilenames = null;
        mUpdateFolder = new File(Environment.getExternalStorageDirectory() + "/" + prefs.getUpdateFolder());
        FilenameFilter f = new UpdateFilter(".zip");
        File[] files = mUpdateFolder.listFiles(f);
        if (mUpdateFolder.exists() && mUpdateFolder.isDirectory() && files != null && files.length > 0) {
            existingFilenames = new ArrayList<String>();
            for (File file : files) {
                existingFilenames.add(file.getName());
            }
            existingFilenames = Collections.synchronizedList(existingFilenames);
            Collections.sort(existingFilenames, Collections.reverseOrder());
        }
        files = null;
        if (Customization.Screenshotsupport) {
            CheckNowUpdateChooserTextThemes.setVisibility(View.GONE);
            CheckNowUpdateChooserThemes.setVisibility(View.GONE);
        }
        CheckNowUpdateChooserTextUpdates.setVisibility(View.GONE);
        CheckNowUpdateChooserUpdates.setVisibility(View.GONE);
        selectUploadButton.setVisibility(View.VISIBLE);
        mUpdatesSpinner.setVisibility(View.VISIBLE);
        DownloadText.setVisibility(View.VISIBLE);
        stableExperimentalInfoUpdates.setVisibility(View.VISIBLE);
        changelogButton.setVisibility(View.VISIBLE);
        if (Customization.Screenshotsupport) {
            btnDownloadTheme.setVisibility(View.VISIBLE);
            mThemesSpinner.setVisibility(View.VISIBLE);
            tvThemeDownloadText.setVisibility(View.VISIBLE);
            stableExperimentalInfoThemes.setVisibility(View.VISIBLE);
            btnThemechangelogButton.setVisibility(View.VISIBLE);
            btnThemeScreenshotButton.setVisibility(View.VISIBLE);
        }
        boolean ThemeUpdateUrlSet = prefs.ThemeUpdateUrlSet();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(R.string.not_new_updates_found_title);
        String showExperimentalRomUpdates;
        String showAllRomUpdates;
        String showExperimentalThemeUpdates = "";
        String showAllThemeUpdates = "";
        Resources res = getResources();
        String trueString = res.getString(R.string.true_string);
        String falseString = res.getString(R.string.false_string);
        if (prefs.showExperimentalRomUpdates()) showExperimentalRomUpdates = trueString; else showExperimentalRomUpdates = falseString;
        if (prefs.showAllRomUpdates()) showAllRomUpdates = trueString; else showAllRomUpdates = falseString;
        if (Customization.Screenshotsupport) {
            if (prefs.showExperimentalThemeUpdates()) showExperimentalThemeUpdates = trueString; else showExperimentalThemeUpdates = falseString;
            if (prefs.showAllThemeUpdates()) showAllThemeUpdates = trueString; else showAllThemeUpdates = falseString;
        }
        experimentalBuildsRomtv.setText(MessageFormat.format(res.getString(R.string.p_allow_experimental_rom_versions_title) + ": {0}", showExperimentalRomUpdates));
        showDowngradesRomtv.setText(MessageFormat.format(res.getString(R.string.p_display_older_rom_versions_title) + ": {0}", showAllRomUpdates));
        if (Customization.Screenshotsupport) {
            experimentalBuildsThemetv.setText(MessageFormat.format(res.getString(R.string.p_allow_experimental_theme_versions_title) + ": {0}", showExperimentalThemeUpdates));
            showDowngradesThemetv.setText(MessageFormat.format(res.getString(R.string.p_display_older_theme_versions_title) + ": {0}", showAllThemeUpdates));
            lastThemeUpdateChecktv.setText(res.getString(R.string.last_update_check_text) + ": " + prefs.getLastUpdateCheckString());
        }
        lastRomUpdateChecktv.setText(res.getString(R.string.last_update_check_text) + ": " + prefs.getLastUpdateCheckString());
        List<UpdateInfo> availableRoms = null;
        List<UpdateInfo> availableThemes = null;
        if (mAvailableUpdates != null) {
            if (mAvailableUpdates.roms != null) availableRoms = mAvailableUpdates.roms;
            if (Customization.Screenshotsupport && mAvailableUpdates.themes != null) availableThemes = mAvailableUpdates.themes;
            if (mAvailableUpdates.incrementalRoms != null) availableRoms.addAll(mAvailableUpdates.incrementalRoms);
        }
        if (availableRoms != null && availableRoms.size() > 0) {
            spAdapterRoms.clear();
            for (UpdateInfo rom : availableRoms) {
                spAdapterRoms.add(rom);
            }
            spAdapterRoms.notifyDataSetChanged();
        } else {
            selectUploadButton.setVisibility(View.GONE);
            mUpdatesSpinner.setVisibility(View.GONE);
            DownloadText.setVisibility(View.GONE);
            stableExperimentalInfoUpdates.setVisibility(View.GONE);
            changelogButton.setVisibility(View.GONE);
            CheckNowUpdateChooserTextUpdates.setVisibility(View.VISIBLE);
            CheckNowUpdateChooserUpdates.setVisibility(View.VISIBLE);
        }
        if (runningOldVersion) selectUploadButton.setEnabled(false);
        if (Customization.Screenshotsupport) {
            if (!ThemeUpdateUrlSet) {
                tvNoThemeUpdateServer.setVisibility(View.VISIBLE);
                btnDownloadTheme.setVisibility(View.GONE);
                mThemesSpinner.setVisibility(View.GONE);
                tvThemeDownloadText.setVisibility(View.GONE);
                stableExperimentalInfoThemes.setVisibility(View.GONE);
                btnThemechangelogButton.setVisibility(View.GONE);
                btnThemeScreenshotButton.setVisibility(View.GONE);
                CheckNowUpdateChooserTextThemes.setVisibility(View.GONE);
                CheckNowUpdateChooserThemes.setVisibility(View.GONE);
            } else if (availableThemes != null && availableThemes.size() > 0) {
                spAdapterThemes.clear();
                for (UpdateInfo theme : availableThemes) {
                    spAdapterThemes.add(theme);
                }
                spAdapterThemes.notifyDataSetChanged();
            } else {
                btnDownloadTheme.setVisibility(View.GONE);
                mThemesSpinner.setVisibility(View.GONE);
                tvThemeDownloadText.setVisibility(View.GONE);
                stableExperimentalInfoThemes.setVisibility(View.GONE);
                btnThemechangelogButton.setVisibility(View.GONE);
                btnThemeScreenshotButton.setVisibility(View.GONE);
                CheckNowUpdateChooserTextThemes.setVisibility(View.VISIBLE);
                CheckNowUpdateChooserThemes.setVisibility(View.VISIBLE);
            }
        }
        if (existingFilenames != null && existingFilenames.size() > 0) {
            localUpdates.clear();
            for (String file : existingFilenames) {
                localUpdates.add(file);
            }
            localUpdates.notifyDataSetChanged();
        } else {
            mNoExistingUpdatesFound.setVisibility(View.VISIBLE);
            mExistingUpdatesSpinner.setVisibility(View.GONE);
            mapplyUpdateButton.setVisibility(View.GONE);
            mdownloadedUpdateText.setVisibility(View.GONE);
            mdeleteOldUpdatesButton.setVisibility(View.GONE);
        }
    }
