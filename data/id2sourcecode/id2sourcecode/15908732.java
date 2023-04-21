            public void run() {
                try {
                    allincrement = 0;
                    String strFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hQuran/img/" + AC.CurrentImageType + ".zip";
                    URL url = new URL(AC.ActivePath + "/img/" + AC.CurrentImageType + ".zip");
                    long startTime = System.currentTimeMillis();
                    Log.d("ImageManager", "downloaded file name>:" + strFile + " - url:" + url.toString());
                    URLConnection ucon = url.openConnection();
                    dialog.setMax(ucon.getContentLength() / 1024);
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is, 8192);
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
                    double current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                        allincrement += current;
                        increment = allincrement / 1024;
                        progressHandler.sendMessage(progressHandler.obtainMessage());
                    }
                    FileOutputStream fos = new FileOutputStream(strFile);
                    fos.write(baf.toByteArray());
                    fos.close();
                } catch (IOException e) {
                    Log.d("ImageManager", "Error: " + e);
                }
                dialog.cancel();
            }
