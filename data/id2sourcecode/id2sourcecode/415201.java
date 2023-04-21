    private static void httpDiskFileResponse(String htmlRootDirectory, String fileName, BufferedOutputStream bout) throws IOException {
        StringBuffer httpResponseHeader = new StringBuffer();
        File fileDescriptor = new File(htmlRootDirectory + fileName);
        if ((!fileDescriptor.exists()) || (fileName.indexOf(":") >= 0) || (fileName.indexOf("..") >= 0) || (fileName.indexOf("/") >= 0) || (fileName.indexOf("\\") >= 0) || (fileName.indexOf("@") >= 0) || (fileName.indexOf("$") >= 0)) {
            httpResponseHeader.append("HTTP/1.0 404 Not Found\r\n");
            httpResponseHeader.append("Connection: close\r\n");
            httpResponseHeader.append("Content-Type: text/html; charset=iso-8859-1\r\n");
            httpResponseHeader.append("\r\n");
            httpResponseHeader.append("<HTML><BODY>\r\n");
            httpResponseHeader.append("<B>Video Applet Server</B><BR>&nbsp;<BR>\r\n");
            httpResponseHeader.append("Error 404: File not found.\r\n");
            httpResponseHeader.append("<BR>&nbsp;<BR>&nbsp;<BR><HR>\r\n");
            httpResponseHeader.append("&copy; 2001 by David Fischer, Bern - Switzerland\r\n");
            httpResponseHeader.append("</BODY></HTML>\r\n");
            bout.write(httpResponseHeader.toString().getBytes());
            bout.flush();
            return;
        }
        httpResponseHeader.append("HTTP/1.0 200 OK\r\n");
        String contentType = getDiskFileContentType(fileName);
        if (contentType != null) httpResponseHeader.append("Content-Type: " + contentType + "\r\n");
        httpResponseHeader.append("Content-Length: " + fileDescriptor.length() + "\r\n");
        httpResponseHeader.append("Connection: close\r\n");
        httpResponseHeader.append("\r\n");
        bout.write(httpResponseHeader.toString().getBytes());
        byte[] fileData = new byte[8192];
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(fileDescriptor));
        int readedBytes = fin.read(fileData);
        while (readedBytes != -1) {
            bout.write(fileData, 0, readedBytes);
            readedBytes = fin.read(fileData);
        }
        bout.flush();
    }
