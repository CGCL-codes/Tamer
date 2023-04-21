                    public void run() {
                        try {
                            URL url = new URL("http://www.lyrc.com.ar/tema1en.php?songname=" + song + "&artist=" + artist);
                            URLConnection urlConnection = url.openConnection();
                            byte[] bytes = new byte[1024];
                            StringBuilder html = new StringBuilder();
                            while (urlConnection.getInputStream().read(bytes) > -1) {
                                html.append(new String(bytes));
                                bytes = new byte[1024];
                            }
                            int indexScript = html.indexOf("<script");
                            while (indexScript > -1) {
                                html = html.delete(indexScript, html.indexOf("</script>", indexScript) + 9);
                                indexScript = html.indexOf("<script");
                            }
                            String css = new File("./ressources/css/lyrics.css").getAbsolutePath();
                            String newHTML = "<html>" + "<head>" + "<link rel=\"stylesheet\" href=\"" + css + "\" style=\"css/text\">" + "</head>" + html.substring(html.indexOf("<body"), html.indexOf("</body>") + 7);
                            browser.setText(newHTML);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
