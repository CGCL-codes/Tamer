    private int returnFileContent(String urlString) throws Exception {
        boolean doRange = false;
        long rangeStart = -1;
        long rangeEnd = -1;
        long dataSent = 0;
        long totalDataToSend = 0;
        urlString = urlString.replaceAll("\\+", "%2B");
        String fileName = URLDecoder.decode(urlString, "UTF-8");
        String[] capPathStrings = store.getCapturePaths();
        boolean capPathFound = false;
        for (int x = 0; x < capPathStrings.length; x++) {
            if (fileName.indexOf("/$path" + x + "$") > -1) {
                capPathFound = true;
                fileName = fileName.replace("/$path" + x + "$", capPathStrings[x]);
                break;
            }
        }
        if (capPathFound == false) {
            fileName = store.getProperty("path.httproot") + fileName;
        }
        File thisFile = new File(fileName);
        String requestedFilePath = thisFile.getCanonicalPath();
        File root = new File(store.getProperty("path.httproot"));
        String rootFilePath = root.getCanonicalPath();
        boolean isOutOfBouns = true;
        for (int x = 0; x < capPathStrings.length; x++) {
            if (requestedFilePath.indexOf(new File(capPathStrings[x]).getCanonicalPath()) == 0) {
                isOutOfBouns = false;
                break;
            }
        }
        if (isOutOfBouns == true && requestedFilePath.indexOf(rootFilePath) < 0) {
            throw new Exception("File out of bounds! (" + thisFile.getCanonicalPath() + ")");
        }
        if (thisFile.getName().equals("dir.list")) {
            StringBuffer data = new StringBuffer();
            data.append("HTTP/1.0 200 OK\n");
            data.append("Content-Type: text/html\n");
            data.append("\n");
            data.append("<html>");
            data.append("<body>\n");
            File[] files = thisFile.getParentFile().listFiles();
            if (files != null) {
                for (int x = 0; x < files.length; x++) {
                    if (files[x].isDirectory() && files[x].isHidden() == false) {
                        data.append("<a href=\"" + files[x].getName() + "/dir.list\">[" + files[x].getName() + "]</a><br>\n");
                    } else if (files[x].isHidden() == false) {
                        data.append("<a href=\"" + files[x].getName() + "\">" + files[x].getName() + "</a><br>\n");
                    }
                }
            } else {
                data.append("Path not found!<br>\n");
            }
            data.append("</body>");
            data.append("</html>\n");
            outStream.write(data.toString().getBytes());
            return 1;
        }
        FileInputStream fi = new FileInputStream(thisFile);
        long fileLength = thisFile.length();
        String rangeString = headers.get("Range");
        if (rangeString != null && rangeString.startsWith("bytes=")) {
            doRange = true;
            rangeString = rangeString.substring("bytes=".length());
            System.out.println(this + " - Doing a Ranged Return (" + rangeString + ")");
            if (rangeString.startsWith("-")) {
                rangeEnd = Long.parseLong(rangeString.substring(1));
                rangeStart = fileLength - rangeEnd;
                rangeEnd = fileLength;
            } else if (rangeString.endsWith("-")) {
                rangeStart = Long.parseLong(rangeString.substring(0, rangeString.length() - 1));
                rangeEnd = fileLength;
            } else {
                String[] bits = rangeString.split("-");
                rangeStart = Long.parseLong(bits[0]);
                rangeEnd = Long.parseLong(bits[1]);
            }
            System.out.println(this + " - Range (" + rangeStart + "-" + rangeEnd + ")");
        }
        int read = 0;
        byte[] bytes = new byte[4096];
        try {
            String header = "";
            if (doRange) {
                header += "HTTP/1.0 206 OK\n";
                header += "Content-Length: " + ((rangeEnd - rangeStart) + 1) + "\n";
                header += "Content-Range: bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength + "\n";
                System.out.println(this + " - Content-Length: " + ((rangeEnd - rangeStart) + 1));
                System.out.println(this + " - Content-Range: bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
            } else {
                header += "HTTP/1.0 200 OK\n";
                header += "Content-Length: " + fileLength + "\n";
            }
            HashMap<String, String> mineTypes = store.getMimeTypes();
            String mineType = "application/octet-stream";
            String ext = "";
            int lastDot = thisFile.getName().lastIndexOf(".");
            if (lastDot > -1 && lastDot != thisFile.length()) {
                ext = thisFile.getName().toLowerCase().substring(lastDot + 1);
                mineType = mineTypes.get(ext);
                if (mineType == null) {
                    mineType = "application/octet-stream";
                }
            }
            header += "Content-Type: " + mineType + "\n";
            header += "Accept-Ranges: bytes\n";
            header += "\n";
            outStream.write(header.getBytes());
            totalDataToSend = 0;
            if (doRange) {
                fi.skip(rangeStart);
                totalDataToSend = (rangeEnd - rangeStart) + 1;
                System.out.println(this + " - totalDataToSend = " + totalDataToSend);
            } else {
                totalDataToSend = fileLength;
            }
            while (true) {
                read = fi.read(bytes);
                if (read == -1) break;
                if ((read + dataSent) > totalDataToSend) {
                    System.out.println(this + " - Data Length Overlap (read=" + read + ", needed=" + (int) (totalDataToSend - dataSent) + ")");
                    read = (int) (totalDataToSend - dataSent);
                }
                outStream.write(bytes, 0, read);
                dataSent += read;
                if (dataSent >= totalDataToSend) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(this + " - ERROR : URL = " + urlData.getReqString());
            String[] keys = headers.keySet().toArray(new String[0]);
            for (int x = 0; x < keys.length; x++) {
                System.out.println(this + " - ERROR : REQUEST HEADER : " + keys[x] + " = " + headers.get(keys[x]));
            }
            System.out.println(this + " - ERROR : doRange = " + doRange);
            System.out.println(this + " - ERROR : totalDataToSend = " + totalDataToSend);
            System.out.println(this + " - ERROR : rangeStart = " + rangeStart);
            System.out.println(this + " - ERROR : rangeEnd = " + rangeEnd);
            System.out.println(this + " - ERROR : dataSent = " + dataSent);
            e.printStackTrace();
        } finally {
            try {
                fi.close();
            } catch (Exception e2) {
            }
        }
        return 1;
    }
