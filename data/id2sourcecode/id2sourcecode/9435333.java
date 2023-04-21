    @Override
    public void run() {
        if (!rawURL.equals("")) {
            try {
                char[] array = message.toCharArray();
                Vector messages = new Vector(1, 1);
                if (array.length < 165) {
                    messages.addElement(message);
                } else {
                    for (int i = 0; i < array.length; i += 165) {
                        int j = i + 165;
                        if (j >= array.length) {
                            j = array.length - 1;
                        }
                        char[] newarr = Arrays.copyOfRange(array, i, j);
                        messages.addElement(new String(newarr));
                    }
                }
                for (int i = 0; i < messages.size(); i++) {
                    String messageToSend = messages.elementAt(i).toString();
                    String[] str = new String[2];
                    str[0] = mobileNumber;
                    str[1] = URLEncoder.encode(messageToSend);
                    MessageFormat mf = new MessageFormat(rawURL);
                    String finalurl = mf.format(rawURL, str);
                    URL urlU = new URL(finalurl);
                    URLConnection ucon = urlU.openConnection();
                    if (ProxySettings.getInstance().isProxyAvailable()) {
                        System.out.println("Proxy settings set................................");
                        ucon.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                    }
                    ucon.setDoOutput(true);
                    OutputStream os = ucon.getOutputStream();
                    InputStream is = ucon.getInputStream();
                    BufferedReader irs = new BufferedReader(new InputStreamReader(is));
                    while (irs.ready()) {
                        System.out.println("***********************************8" + irs.readLine());
                    }
                    is.close();
                    os.close();
                }
                retint = SEND_SUCCESSFUL;
            } catch (Exception exp) {
                retint = ERR_SMS_SERVER_ERROR;
                exp.printStackTrace();
            }
        }
    }
