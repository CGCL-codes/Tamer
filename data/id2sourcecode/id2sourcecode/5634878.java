                public void run() {
                    try {
                        URL url = new URL(URL_STR);
                        URLConnection connection = url.openConnection(getProxy());
                        connection.setRequestProperty("User-Agent", USER_AGENT);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                        output.writeBytes("app=" + URLEncoder.encode(KeyboardHero.APP_TITLE, "UTF-8") + "&req=add&code=" + URLEncoder.encode(finalCodeline, "UTF-8"));
                        output.flush();
                        output.close();
                        DataInputStream input = new DataInputStream(connection.getInputStream());
                        input.close();
                    } catch (Exception e) {
                        Util.error(Util.getMsg("CannotToplist"), Util.getMsg("CannotToplist2"));
                    }
                }
