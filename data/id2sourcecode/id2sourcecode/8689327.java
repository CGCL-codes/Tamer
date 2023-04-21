    public void startSession() throws MalformedURLException, IOException {
        this.urlConnection = (HttpURLConnection) new URL(WRITE_SMS_URL).openConnection();
        InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
        ParserDelegator parser = new ParserDelegator();
        HTMLEditorKit.ParserCallback cb = new MegafonCaptchaSeeker();
        parser.parse(isr, cb, false);
        isr.close();
        this.captchaID = ((MegafonCaptchaSeeker) cb).getCaptchaID();
        logger.debug("Captcha ID : " + this.captchaID);
        captchaFile = new File("captcha" + RandomGenerator.getInt() + ".jpeg");
        FileDownloader.download(CAPTCHA_URL + this.captchaID, captchaFile.getAbsolutePath());
    }
