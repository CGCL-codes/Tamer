    @Override
    public Void doInBackground() {
        int progress = 0;
        setProgress(0);
        this.updating = true;
        try {
            String expectedChecksum = getExpectedChecksum(latestVersion);
            boolean download = true;
            File file = new File(String.format(downloadedFileNameFormat, this.latestVersion));
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                byte[] buf = new byte[1024];
                int read;
                while ((read = in.read(buf)) != -1) {
                    md.update(buf, 0, read);
                }
                in.close();
                String fileChecksum = convertToHex(md.digest());
                if (expectedChecksum != null && expectedChecksum.equalsIgnoreCase(fileChecksum)) {
                    download = false;
                    checksumMatches = true;
                }
            }
            if (download) {
                FileInfo info = getFileInputStreamAndLength(latestVersion);
                String fileChecksum;
                {
                    FileOutputStream out = new FileOutputStream(file);
                    InputStream raw = info.inputStream;
                    InputStream in = new BufferedInputStream(raw);
                    byte[] buf = new byte[1024];
                    int bytesRead = 0;
                    int offset = 0;
                    while ((bytesRead = in.read(buf)) != -1) {
                        md.update(buf, 0, bytesRead);
                        out.write(buf, 0, bytesRead);
                        offset += bytesRead;
                        progress = (int) ((offset / (float) info.length) * 100);
                        setProgress(progress);
                    }
                    fileChecksum = convertToHex(md.digest());
                    in.close();
                    out.close();
                    if (offset != info.length) {
                        throw new IOException("Only read " + offset + " bytes; Expected " + info.length + " bytes");
                    }
                }
                checksumMatches = expectedChecksum == null || expectedChecksum.equalsIgnoreCase(fileChecksum);
            }
            this.jarFile = file.getName();
        } catch (MalformedURLException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        } catch (NoSuchAlgorithmException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        }
        return null;
    }
