    public void run() {
        String filename = "update/files.lst";
        String s;
        Integer i = 0;
        Integer total = 0;
        Integer length = -1;
        Float done = (float) 0.0;
        Float totallength = (float) 0.0;
        Float completed = (float) 0;
        Float speed, percent, percent2 = (float) 0.0;
        Long start, now, diff;
        URL url;
        HttpURLConnection conn;
        InputStream sin;
        OutputStream fout;
        File updatePath;
        byte[] buffer = new byte[4096];
        ui.disableAllButtons();
        ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Hole Fileliste... ");
        try {
            url = new URL("http://tempuspre.sourceforge.net/updates/" + filename);
            conn = HttpConnectionFactory.createConnection(url);
            conn.connect();
            sin = conn.getInputStream();
            updatePath = new File(PATH + "update");
            if (!updatePath.exists()) {
                updatePath.mkdirs();
            }
            fout = new FileOutputStream(new File(PATH + "/" + filename));
            total = conn.getContentLength();
            start = System.currentTimeMillis();
            now = (long) 0;
            diff = (long) 0;
            length = sin.read(buffer, 0, buffer.length);
            while (length != -1) {
                fout.write(buffer, 0, length);
                completed += (float) length;
                percent = completed / total * 100;
                now = System.currentTimeMillis();
                diff = now - start;
                speed = completed / diff;
                speed *= 1000;
                speed /= 1024.0f;
                speed = (((float) Math.round(speed * 10)) / 10);
                ui.getJProgressBar1().setValue(Math.round(percent));
                ui.getJProgressBar2().setValue(Math.round(percent));
                ui.getJProgressBar1().setString("" + ((float) Math.round(percent * 10)) / 10 + "% - " + speed + "kb/s");
                ui.getJProgressBar2().setString("" + ((float) Math.round(percent * 10)) / 10 + "%");
                ui.getJProgressBar1().setStringPainted(true);
                ui.getJProgressBar2().setStringPainted(true);
                ui.getJProgressBar1().paint(ui.getJProgressBar1().getGraphics());
                ui.getJProgressBar2().paint(ui.getJProgressBar2().getGraphics());
                length = sin.read(buffer, 0, buffer.length);
            }
            fout.flush();
            fout.close();
            ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "ok \r\n");
            ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Prüfe auf Updates... ");
            totallength = (float) 0.0;
            FileWriter fw = new FileWriter(new File(PATH + "update/updates.lst"));
            BufferedReader fin = new BufferedReader(new FileReader(PATH + "update/files.lst"));
            while ((s = fin.readLine()) != null) {
                String[] arr = s.split("\\|");
                File updateFile = new File(PATH + arr[0]);
                if (updateFile.exists() == true) {
                    MessageDigest messagedigest = MessageDigest.getInstance("md5");
                    DigestInputStream dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(PATH + arr[0])), messagedigest);
                    StringBuffer hash = new StringBuffer();
                    byte md[] = new byte[8192];
                    int n = 0;
                    while ((n = dis.read(md)) > -1) {
                        messagedigest.update(md, 0, n);
                    }
                    byte[] digest = messagedigest.digest();
                    for (int j = 0; j < digest.length; j++) {
                        hash.append(Integer.toHexString(0xff & digest[j]));
                    }
                    String checksum = hash.toString();
                    if (!checksum.equals(arr[2])) {
                        fw.write(arr[0] + "\r\n");
                        totallength += Float.valueOf(arr[1]);
                    }
                } else {
                    fw.write(arr[0] + "\r\n");
                    totallength += Float.valueOf(arr[1]);
                }
                i++;
            }
            fw.close();
            fin.close();
            if (totallength != 0.0) {
                ui.getJProgressBar2().setValue(0);
                ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "ok \r\n");
                ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Hole Updates... \r\n");
                s = "";
                BufferedReader in = new BufferedReader(new FileReader(PATH + "update/updates.lst"));
                while ((s = in.readLine()) != null) {
                    ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Hole " + s + " ");
                    url = new URL("http://tempuspre.sourceforge.net/updates/" + s);
                    conn = HttpConnectionFactory.createConnection(url);
                    conn.connect();
                    sin = conn.getInputStream();
                    fout = new FileOutputStream(new File(PATH + "/" + s));
                    total = conn.getContentLength();
                    completed = (float) 0.0;
                    done = (float) ui.getJProgressBar2().getValue();
                    length = -1;
                    start = System.currentTimeMillis();
                    now = (long) 0;
                    diff = (long) 0;
                    speed = (float) 0.0;
                    percent = (float) 0.0;
                    percent2 = (float) 0.0;
                    length = sin.read(buffer, 0, buffer.length);
                    while (length != -1) {
                        fout.write(buffer, 0, length);
                        completed += (float) length;
                        percent = completed / total * 100;
                        now = System.currentTimeMillis();
                        diff = now - start;
                        speed = completed / diff;
                        speed *= 1000;
                        speed /= 1024.0f;
                        speed = (((float) Math.round(speed * 10)) / 10);
                        ui.getJProgressBar1().setValue(Math.round(percent));
                        ui.getJProgressBar1().setString("" + ((float) Math.round(percent * 10)) / 10 + "% - " + speed + "kb/s");
                        ui.getJProgressBar1().setStringPainted(true);
                        ui.getJProgressBar1().paint(ui.getJProgressBar1().getGraphics());
                        if (totallength != 0.0) {
                            percent2 = done + (completed / totallength * 100);
                        }
                        ui.getJProgressBar2().setValue(Math.round(percent2));
                        ui.getJProgressBar2().setString("" + ((float) Math.round(percent2 * 10)) / 10 + "%");
                        ui.getJProgressBar2().setStringPainted(true);
                        ui.getJProgressBar2().paint(ui.getJProgressBar2().getGraphics());
                        length = sin.read(buffer, 0, buffer.length);
                    }
                    fout.flush();
                    fout.close();
                    ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "ok \r\n");
                }
                ui.getJProgressBar1().setValue(ui.getJProgressBar1().getMaximum());
                ui.getJProgressBar1().setString("100%");
                ui.getJProgressBar1().setStringPainted(true);
                ui.getJProgressBar1().paint(ui.getJProgressBar1().getGraphics());
                ui.getJProgressBar2().setValue(ui.getJProgressBar2().getMaximum());
                ui.getJProgressBar2().setString("100%");
                ui.getJProgressBar2().setStringPainted(true);
                ui.getJProgressBar2().paint(ui.getJProgressBar2().getGraphics());
                ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Updates fertig. \r\n");
            } else {
                ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "ok \r\nKeine neuen Updates vorhanden. \r\n");
            }
        } catch (Exception e) {
            ui.getJTextPane2().setText(ui.getJTextPane2().getText() + "Fehler: \r\n" + e + "\r\n");
            e.printStackTrace();
        }
        ui.enableAllButtons();
    }
