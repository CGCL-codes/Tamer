    public static void downloadFile(String webSite, String outputDirectory, String outputFileName, boolean needCookie, String cookieString, boolean fastMode, int retryTimes, boolean gzipEncode, boolean forceDownload) {
        int fileGotSize = 0;
        if (CommonGUI.stateBarDetailMessage == null) {
            CommonGUI.stateBarMainMessage = "下載網頁進行分析 : ";
            CommonGUI.stateBarDetailMessage = outputFileName + " ";
        }
        if (Run.isAlive || forceDownload) {
            try {
                ComicDownGUI.stateBar.setText(webSite + " 連線中...");
                URL url = new URL(webSite);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
                connection.setFollowRedirects(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept-Language", "zh-cn");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Pragma", "no-cache");
                connection.setRequestProperty("Host", "biz.finance.sina.com.cn");
                connection.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setConnectTimeout(10000);
                if (needCookie) {
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Cookie", cookieString);
                }
                int responseCode = 0;
                if ((fastMode && connection.getResponseCode() != 200) || (fastMode && connection.getContentLength() == 10771)) {
                    return;
                }
                Timer timer = new Timer();
                if (SetUp.getTimeoutTimer() > 0) {
                    timer.schedule(new TimeoutTask(), SetUp.getTimeoutTimer() * 1000);
                }
                tryConnect(connection);
                int fileSize = connection.getContentLength() / 1000;
                if (Common.isPicFileName(outputFileName) && (fileSize == 21 || fileSize == 22)) {
                    Common.debugPrintln("似乎連到盜連圖，停一秒後重新連線......");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException iex) {
                    }
                    tryConnect(connection);
                }
                if (connection.getResponseCode() != 200) {
                    Common.errorReport("錯誤回傳碼(responseCode): " + connection.getResponseCode() + " : " + webSite);
                    return;
                }
                Common.checkDirectory(outputDirectory);
                OutputStream os = new FileOutputStream(outputDirectory + outputFileName);
                InputStream is = null;
                if (gzipEncode && fileSize < 17) {
                    try {
                        is = new GZIPInputStream(connection.getInputStream());
                    } catch (IOException ex) {
                        is = connection.getInputStream();
                    }
                } else {
                    is = connection.getInputStream();
                }
                Common.debugPrint("(" + fileSize + " k) ");
                String fileSizeString = fileSize > 0 ? "" + fileSize : " ? ";
                byte[] r = new byte[1024];
                int len = 0;
                while ((len = is.read(r)) > 0 && (Run.isAlive || forceDownload)) {
                    if (fileSize > 1024 || !Flag.timeoutFlag) {
                        os.write(r, 0, len);
                    } else {
                        break;
                    }
                    fileGotSize += (len / 1000);
                    if (Common.withGUI()) {
                        int percent = 100;
                        String downloadText = "";
                        if (fileSize > 0) {
                            percent = (fileGotSize * 100) / fileSize;
                            downloadText = fileSizeString + "Kb ( " + percent + "% ) ";
                        } else {
                            downloadText = fileSizeString + " Kb ( " + fileGotSize + "Kb ) ";
                        }
                        ComicDownGUI.stateBar.setText(CommonGUI.stateBarMainMessage + CommonGUI.stateBarDetailMessage + " : " + downloadText);
                    }
                }
                is.close();
                os.flush();
                os.close();
                if (Common.withGUI()) {
                    ComicDownGUI.stateBar.setText(CommonGUI.stateBarMainMessage + CommonGUI.stateBarDetailMessage + " : " + fileSizeString + "Kb ( 100% ) ");
                }
                connection.disconnect();
                int realFileGotSize = (int) new File(outputDirectory + outputFileName).length() / 1000;
                if (realFileGotSize + 1 < fileGotSize && retryTimes > 0) {
                    String messageString = realFileGotSize + " < " + fileGotSize + " -> 等待兩秒後重新嘗試下載" + outputFileName + "（" + retryTimes + "/" + SetUp.getRetryTimes() + "）";
                    Common.debugPrintln(messageString);
                    ComicDownGUI.stateBar.setText(messageString);
                    Thread.sleep(2000);
                    downloadFile(webSite, outputDirectory, outputFileName, needCookie, cookieString, fastMode, retryTimes - 1, gzipEncode, false);
                }
                if (fileSize < 1024 && Flag.timeoutFlag) {
                    new File(outputDirectory + outputFileName).delete();
                    Common.debugPrintln("刪除不完整檔案：" + outputFileName);
                    ComicDownGUI.stateBar.setText("下載逾時，跳過" + outputFileName);
                }
                timer.cancel();
                Flag.timeoutFlag = false;
                Common.debugPrintln(webSite + " downloads successful!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
