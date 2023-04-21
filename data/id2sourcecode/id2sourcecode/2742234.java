    private boolean isReCaptchaKeyValid(String reCaptchaPublicKey, String recaptchaPrivateKey) {
        boolean isValid = false;
        if (reCaptchaPublicKey != null && recaptchaPrivateKey != null) {
            ReCaptcha c = ReCaptchaFactory.newReCaptcha(reCaptchaPublicKey, recaptchaPrivateKey, false);
            String recaptchaHtml = c.createRecaptchaHtml(null, null);
            Pattern pattern = Pattern.compile(".*src=\"(.*)\".*");
            Matcher matcher = pattern.matcher(recaptchaHtml);
            matcher.find();
            String match = matcher.group(1);
            try {
                URL url = new URL(match);
                URLConnection urlConnection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer text = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    text.append(inputLine);
                }
                in.close();
                String responseText = text.toString();
                if (!responseText.contains("Input error")) {
                    isValid = true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }
