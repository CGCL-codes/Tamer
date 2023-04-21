    public HTTPHeaders(DataInputStream br) throws IOException {
        super();
        line1 = br.readLine();
        System.out.println(Dates.FormatDate(Calendar.getInstance().getTime(), Dates.UKDATE_FORMAT_WITH_TIME) + " " + line1);
        request = new URLRequest(line1);
        String line = br.readLine();
        String csv[];
        while (line != null) {
            csv = line.split(":");
            StringBuffer str = new StringBuffer();
            for (int i = 1; i < csv.length; i++) {
                str.append(csv[i].trim());
                if (i < csv.length - 1) {
                    str.append(":");
                }
            }
            KeyValuePair kvp = new KeyValuePair(csv[0].trim(), str);
            super.put(kvp.key.toLowerCase().trim(), kvp.value);
            if (kvp.key.equalsIgnoreCase("cookie")) {
                String ckcsv[] = kvp.value.toString().split(";");
                for (int i = 0; i < ckcsv.length; i++) {
                    String ck2[] = ckcsv[i].trim().split("=");
                    if (ck2.length > 1) {
                        String key = ck2[0];
                        String value = ck2[1];
                        cookies.put(key, value);
                    }
                }
            } else if (kvp.key.equalsIgnoreCase("Connection")) {
                this.keep_alive = kvp.value.toString().equalsIgnoreCase("keep-alive");
            }
            line = br.readLine();
            if (line == null) {
                break;
            }
            if (line.length() <= 2) {
                break;
            }
        }
        if (this.containsKey("content-length")) {
            if (this.getContentLength() > 0) {
                if (this.getContentType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
                    StringBuffer data = new StringBuffer();
                    int tot_read = 0;
                    byte tmp[] = new byte[4096];
                    char c[] = new char[this.getContentLength()];
                    while (tot_read < this.getContentLength()) {
                        int read = br.read(tmp, 0, Math.min(tmp.length, c.length - tot_read));
                        tot_read += read;
                        data.append(new String(tmp, 0, read));
                    }
                    postdata = URLRequest.DecodeParams(data);
                } else if (this.getContentType().indexOf("multipart/form-data") >= 0) {
                    uploaded_files = new HashMap<String, UploadFileData>();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int tot_read = 0;
                    byte tmp[] = new byte[4096];
                    while (tot_read < this.getContentLength()) {
                        int read = br.read(tmp);
                        if (read <= 0) {
                            break;
                        }
                        tot_read += read;
                        out.write(tmp, 0, read);
                    }
                    String boundary = null;
                    UploadFileData filedata = new UploadFileData();
                    byte data[] = out.toByteArray();
                    int pos = 0;
                    int prev_pos = 0;
                    boolean reading_text = true;
                    ByteArrayOutputStream file = new ByteArrayOutputStream();
                    while (pos < data.length) {
                        if (reading_text) {
                            if (data[pos] == 13) {
                                String t = new String(data, prev_pos, pos - prev_pos);
                                pos += 2;
                                prev_pos = pos;
                                if (boundary == null) {
                                    boundary = t;
                                } else {
                                    if (filedata.getDisposition() == null) {
                                        filedata.setDisposition(t);
                                        if (filedata.hasFilename() == false) {
                                            pos += 1;
                                            reading_text = false;
                                        }
                                    } else {
                                        filedata.setType(t);
                                        pos += 1;
                                        reading_text = false;
                                    }
                                }
                            }
                        } else {
                            String test = new String(data, pos + 2, boundary.length());
                            if (test.equals(boundary)) {
                                if (filedata.hasFilename()) {
                                    if (file.size() > 0) {
                                        String unique_filename = IOFunctions.GetUniqueFilenameAndPath("./webroot/temp_uploaded_files", filedata.getFileExtention());
                                        filedata.setNewFilename(unique_filename);
                                        FileOutputStream fos = new FileOutputStream(unique_filename);
                                        fos.write(file.toByteArray());
                                        fos.close();
                                        this.uploaded_files.put(filedata.getName(), filedata);
                                    } else {
                                    }
                                } else {
                                    if (postdata == null) {
                                        postdata = new HashMap<String, StringBuffer>();
                                    }
                                    this.postdata.put(filedata.getName(), new StringBuffer(new String(file.toByteArray())));
                                }
                                pos += boundary.length() + 2;
                                file = new ByteArrayOutputStream();
                                filedata = new UploadFileData();
                                reading_text = true;
                                pos += 2;
                                prev_pos = pos;
                            } else {
                                file.write(data[pos]);
                            }
                        }
                        pos++;
                    }
                }
            }
        }
    }
