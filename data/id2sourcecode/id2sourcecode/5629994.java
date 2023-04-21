    public void doInstallAction(CocoonInstallComponent parent) {
        boolean complete = true;
        String filename = null;
        try {
            URL url = new URL(this.url);
            filename = url.getFile();
            if (filename.lastIndexOf("/") > -1) {
                filename = filename.substring(filename.lastIndexOf("/") + 1);
                System.out.println("filename=" + filename + " newlocation=" + installdir + File.separator + filename);
            }
            URLConnection connection = url.openConnection();
            filelength = connection.getContentLength();
            step.setText("Download now");
            progressbar.setMinimum(0);
            progressbar.setMaximum(filelength);
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(installdir + File.separator + filename));
            int current = 0;
            int process = 0;
            int length = -1;
            byte[] bytes = new byte[1024];
            while ((length = in.read(bytes, 0, bytes.length)) != -1) {
                out.write(bytes, 0, length);
                current += length;
                progressbar.setValue(current);
                this.bytes.setText(current + "/" + filelength);
            }
            in.close();
            out.flush();
            out.close();
            in = null;
            out = null;
            complete = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (complete && (filename != null)) {
            try {
                ZipFile ziparchiv = new ZipFile(installdir + File.separator + filename);
                unpackdir = null;
                step.setText("Unpack");
                progressbar.setValue(0);
                progressbar.setMinimum(0);
                int process = 0;
                int progress = 0;
                Enumeration e = ziparchiv.entries();
                progressbar.setMaximum(ziparchiv.size());
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    if (entry.isDirectory()) {
                        if (unpackdir == null) {
                            unpackdir = installdir + File.separator + entry.getName();
                        }
                        File dir = new File(installdir + File.separator + entry.getName());
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        process++;
                        progressbar.setValue(process);
                    } else {
                        BufferedInputStream in = new BufferedInputStream(ziparchiv.getInputStream(entry));
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(installdir + File.separator + entry.getName()));
                        int current = 0;
                        int length = -1;
                        byte[] bytes = new byte[1024];
                        while ((length = in.read(bytes, 0, bytes.length)) != -1) {
                            out.write(bytes, 0, length);
                            current += length;
                            this.bytes.setText(current + "/" + entry.getSize());
                        }
                        in.close();
                        out.flush();
                        out.close();
                        in = null;
                        out = null;
                        process++;
                        progressbar.setValue(process);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        parent.setDownloadComplete(true);
    }
