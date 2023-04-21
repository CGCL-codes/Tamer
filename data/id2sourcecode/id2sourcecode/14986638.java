    public SubtitleFile napiSearchForSubtitles(String language) throws UnknownHostException, MalformedURLException, FileNotFoundException, IOException, NoSuchAlgorithmException, LanguageNotSupportedException, SevenZipException, SubtitlesNotFoundException, InterruptedException, TimeoutException {
        if (language.equals("pl") == false && language.equals("en") == false) {
            throw new SubtitlesNotFoundException(String.format(Bundles.subgetBundle.getString("No_%s_subtitles_to_%s_found_Napiprojekt_base."), Language.XXToName(language), file.getName()));
        }
        if (Global.check7z() == false) {
            throw (new SevenZipException(Bundles.subgetBundle.getString("7zip_executable_not_found")));
        }
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        URL url = new URL(getNapiDownloadUrl(language));
        String subtitles7z = Global.getPathToTmpDir() + File.separator + napiMd5sum + ".7z";
        out = new BufferedOutputStream(new FileOutputStream(subtitles7z));
        conn = url.openConnection(Global.getProxy());
        in = Timeouts.getInputStream(conn);
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
        }
        in.close();
        out.close();
        File subtitles7zFile = new File(subtitles7z);
        if (subtitles7zFile.length() <= 4) {
            subtitles7zFile.delete();
            SubtitlesNotFoundException e = new SubtitlesNotFoundException(String.format(Bundles.subgetBundle.getString("No_%s_subtitles_to_%s_found_Napiprojekt_base."), Language.XXToName(language), file.getName()));
            throw e;
        }
        SubtitleFile sub = new SubtitleFile(subtitles7z, outputSubsFileName, outputSubsDir.getAbsolutePath(), language);
        sub.napiUnpack();
        sub.detectFormat();
        sub.setFromBase(Global.SubDataBase.BASE_NAPI);
        sub.setDisplayName(outputSubsFileName);
        return sub;
    }
