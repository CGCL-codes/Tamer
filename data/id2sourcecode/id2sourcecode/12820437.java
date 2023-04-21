    public void napiUploadSubtitles() throws InterruptedException {
        if (uploadNapi) {
            Logging.logger.finer(Bundles.subgetBundle.getString("Starting_upload_to_NAPI..."));
            for (int i = 0; i < vspairs.size(); ++i) {
                try {
                    VideoFile vid = vspairs.get(i).getVideo();
                    SubtitleFile sub = vspairs.get(i).getSubtitle();
                    if (vid.getNapiHash().equals("")) {
                        vid.setNapiHash();
                    }
                    if (sub.getNapiMd5Sum().equals("")) {
                        sub.setNapiMd5sum();
                    }
                    sub.setNapiMd5sum();
                    sub.napiPack(vid.getNapiMd5sum());
                    if (vid.getHasInfo() == false) {
                        try {
                            vid.getVideoInfo();
                        } catch (BrokenAviHeaderException e) {
                        } catch (NotSupportedContainerException e) {
                        }
                    }
                    URLConnection conn = null;
                    ClientHttpRequest httpPost = null;
                    InputStreamReader responseStream = null;
                    URL url;
                    if (vid.getHasInfo()) {
                        url = new URL("http://www.napiprojekt.pl/unit_napisy/upload.php?" + "m_length=" + vid.getTimeString() + "&m_resolution=" + vid.getResolutionString() + "&m_fps=" + vid.getFpsString() + "&m_hash=" + vid.getNapiMd5sum() + "&m_filesize=" + vid.getFile().length());
                    } else {
                        url = new URL("http://www.napiprojekt.pl/unit_napisy/upload.php?" + "&m_hash=" + vid.getNapiMd5sum() + "&m_filesize=" + vid.getFile().length());
                    }
                    conn = url.openConnection(Global.getProxy());
                    conn.setRequestProperty("User-Agent", Global.USER_AGENT);
                    httpPost = new ClientHttpRequest(conn);
                    httpPost.setParameter("nick", Global.getNapiSessionUserName());
                    httpPost.setParameter("pass", Global.getNapiSessionUserPass());
                    httpPost.setParameter("l", subLanguage);
                    httpPost.setParameter("m_filename", vid.getFile().getName());
                    httpPost.setParameter("t", vid.getNapiHash());
                    httpPost.setParameter("s_hash", sub.getNapiMd5Sum());
                    httpPost.setParameter("v", "other");
                    httpPost.setParameter("kmt", comments);
                    httpPost.setParameter("poprawka", napiCorrection ? "true" : "false");
                    httpPost.setParameter("MAX_FILE_SIZE", "512000");
                    httpPost.setParameter("plik", sub.getCompressedFile(), "subtitles/zip");
                    responseStream = new InputStreamReader(httpPost.post(), "Cp1250");
                    BufferedReader responseReader = new BufferedReader(responseStream);
                    String response = responseReader.readLine();
                    if (response.indexOf("NPc0") != 0 && response.indexOf("NPc2") != 0 && response.indexOf("NPc3") != 0) {
                        napiWarnings.add("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_Could_not_upload_subtitles_to_NAPI_database."));
                    }
                    sub.getCompressedFile().delete();
                } catch (SevenZipException ex) {
                    napiWarnings.add("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_7zip_error._Could_not_upload_subtitles_to_NAPI_database."));
                    Logging.logger.severe("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_7zip_error._Could_not_upload_subtitles_to_NAPI_database."));
                } catch (IOException ex) {
                    napiWarnings.add("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_Connection_error._Could_not_upload_subtitles_to_NAPI_database."));
                    Logging.logger.severe("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_Connection_error._Could_not_upload_subtitles_to_NAPI_database."));
                } catch (NoSuchAlgorithmException ex) {
                    napiWarnings.add("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_MD5_error._Could_not_upload_subtitles_to_NAPI_database."));
                    Logging.logger.severe("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_MD5_error._Could_not_upload_subtitles_to_NAPI_database."));
                } catch (TimeoutException ex) {
                    napiWarnings.add("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_Timeout_error._Could_not_upload_subtitles_to_NAPI_database."));
                    Logging.logger.severe("CD" + String.valueOf(i + 1) + Bundles.subgetBundle.getString(":_Timeout_error._Could_not_upload_subtitles_to_NAPI_database."));
                }
            }
        }
    }
