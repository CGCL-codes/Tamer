    private Result postFile(final File f, boolean isFirst, boolean isLast) throws InterruptedException, UnsupportedEncodingException {
        for (; ; ) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lifecycle.filePostAttempt(f);
                }
            });
            boolean doNotTry = false;
            if (!f.isFile() || !f.canRead()) {
                doNotTry = true;
                latestResponse = "File " + f + " is nonexistant, unreadable, or not a regular file.";
            } else if (f.length() > MAX_FILE_SIZE) {
                doNotTry = true;
                latestResponse = "File " + f + " is larger than " + MAX_FILE_SIZE + " bytes.";
            }
            if (doNotTry) {
                latestResult = Result.Rejected;
                extraChallenges++;
                break;
            }
            Captcha captcha = getCaptcha();
            MultipartEntity formPost = generateFormPost(f, captcha, isFirst, isLast);
            HttpPost post = new HttpPost(action);
            post.setEntity(formPost);
            try {
                HttpResponse r = httpclient.execute(post);
                InputStream is = r.getEntity().getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                int ch;
                StringBuilder s = new StringBuilder();
                while ((ch = isr.read()) != -1) s.append((char) ch);
                latestResponse = s.toString();
                if (r.getStatusLine().getStatusCode() == 200) latestResult = interpretResponse(); else latestResult = Result.TotalFailure;
            } catch (ClientProtocolException e) {
                latestResponse = e.toString();
                latestResult = Result.TotalFailure;
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) throw new InterruptedException();
                latestResponse = e.toString();
                latestResult = Result.TotalFailure;
            }
            if (latestResult == Result.Success || latestResult == Result.TotalFailure || latestResult == Result.Rejected) break;
            if (extraChallenges == 0) captchas.getMoreChallenges(1); else extraChallenges--;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lifecycle.filePostAttempted(f, latestResult);
                }
            });
            if (latestResult == Result.Flooded) {
                synchronized (this) {
                    if (!paused) wait(postDelay);
                    if (paused) return latestResult;
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                lifecycle.filePostDone(f, latestResult);
            }
        });
        if (latestResult == Result.Success && !isLast) {
            synchronized (this) {
                if (!paused) wait(postDelay);
            }
        }
        return latestResult;
    }
