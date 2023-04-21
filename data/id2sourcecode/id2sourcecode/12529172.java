    public static void download(String url, Shell parentShell) throws Exception {
        FileOutputStream fos = null;
        File outFile = null;
        try {
            url = url.replace('\\', '/');
            outFile = new File(url.substring(url.lastIndexOf("/")));
            FileDialog fileDialog = new FileDialog(parentShell, SWT.SAVE);
            fileDialog.setFileName(outFile.getName());
            String str = fileDialog.open();
            if (str == null) return;
            outFile = new File(str);
            HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
            httpcon.setInstanceFollowRedirects(true);
            if (httpcon.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) VidUtil.openURL(url);
            int max = httpcon.getContentLength();
            BufferedInputStream in = new BufferedInputStream(httpcon.getInputStream());
            fos = new FileOutputStream(outFile);
            ProgressMonitorDialog progress = new ProgressMonitorDialog(parentShell);
            progress.create();
            progress.getShell().setText(I18N.translate("dialog.expDownload.title"));
            Downloader dw = new Downloader(I18N.translate("dialog.expDownload.task") + ": " + url, in, fos, max);
            progress.setBlockOnOpen(false);
            progress.setOpenOnRun(true);
            progress.setCancelable(true);
            progress.run(true, true, dw);
        } catch (InterruptedException cancel) {
            fos.close();
            outFile.delete();
        } catch (Exception e) {
            throw new ExplicantoException(new WSResponse(2000, "\"" + url + "\" konnte nicht geladen werden:\n" + e.getMessage(), null, null));
        }
    }
