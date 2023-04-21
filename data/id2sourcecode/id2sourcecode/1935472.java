    public boolean uploadPhoto(File photoFile, int albumId) {
        boolean status = true;
        UploadResp resp = new UploadResp();
        assertLoggedIn();
        String fileName = photoFile.getName();
        String fileLength = Long.toString(photoFile.length());
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (Exception e) {
            System.out.println("uploadPhoto exception: " + e.toString());
            return false;
        }
        if (this.debug) {
            System.out.println("uploadPhoto: " + fileName + " " + encodedFileName + " filelen " + fileLength);
        }
        try {
            URL url = new URL(UPLOAD_URL + fileName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Length", fileLength);
            conn.setRequestProperty("X-Smug-SessionID", this.sessionId);
            conn.setRequestProperty("X-Smug-Version", API_VERSION);
            conn.setRequestProperty("X-Smug-ResponseType", "REST");
            conn.setRequestProperty("X-Smug-AlbumID", Integer.toString(albumId));
            conn.setRequestProperty("X-Smug-FileName", encodedFileName);
            FileInputStream photoStream = new FileInputStream(photoFile);
            MessageDigest md5summer;
            try {
                md5summer = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.toString());
                return false;
            }
            int numRead;
            int totalBytes = 0;
            while ((numRead = photoStream.read(photoBuffer)) != -1) {
                md5summer.update(photoBuffer, 0, numRead);
                totalBytes += numRead;
            }
            byte[] md5sum = md5summer.digest();
            String md5sumStr = "";
            for (int i = 0; i < md5sum.length; i++) {
                md5sumStr += Integer.toHexString((md5sum[i] & 0xff) + 0x100).substring(1);
            }
            conn.setRequestProperty("Content-MD5", md5sumStr);
            if (this.debug) {
                System.out.println("MD5SUM: " + md5sumStr);
            }
            OutputStream connStream = conn.getOutputStream();
            if (numRead == totalBytes) {
                connStream.write(photoBuffer, 0, totalBytes);
            } else {
                photoStream = new FileInputStream(photoFile);
                while ((numRead = photoStream.read(photoBuffer)) != -1) {
                    connStream.write(photoBuffer, 0, numRead);
                }
            }
            connStream.flush();
            try {
                parseResp(conn.getInputStream(), new UploadRespHandler(resp));
                status = resp.isOk();
            } catch (Exception e) {
                if (this.debug) {
                    System.out.println("exception processing upload response: " + e.toString());
                }
                status = false;
            }
            if (this.debug) {
                System.out.println("HTTP Response Code: " + conn.getResponseCode());
                System.out.println("HTTP Response Message: " + conn.getResponseMessage());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.toString());
            status = false;
        }
        if (this.debug) {
            System.out.println("uploadPhoto: " + fileName + " status " + status);
        }
        return status;
    }
