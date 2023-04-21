    public Reader(String fileName, String mime, byte[] data) throws Exception {
        this.fileName = fileName;
        this.mime = mime;
        this.data = data;
        this.result = new PhoneParserResultDTO();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        if (fileName.endsWith(CSVPOSTFIX)) {
            this.br = new BufferedReader(new InputStreamReader(in));
            parse(fileName);
            br.close();
        } else {
            if (fileName.endsWith(XLSPOSTFIX)) {
                Workbook wb = Workbook.getWorkbook(in);
                for (int sh = 0; sh < wb.getNumberOfSheets(); sh++) {
                    File newCsv = File.createTempFile(TMPPREFIX, CSVPOSTFIX);
                    FileWriter out = new FileWriter(newCsv);
                    try {
                        xlsToCsv(wb, out, sh);
                    } catch (Exception e) {
                        result.addWrongDataFile(fileName + "/sheet_" + sh);
                    }
                    out.close();
                    this.br = new BufferedReader(new FileReader(newCsv));
                    parse(fileName + "/sheet_" + sh);
                    br.close();
                    newCsv.delete();
                }
            } else {
                if (fileName.endsWith(ZIPPOSTFIX)) {
                    File f = File.createTempFile(TMPPREFIX, ZIPPOSTFIX);
                    FileOutputStream out = new FileOutputStream(f);
                    ZipFile zf;
                    ZipInputStream zin;
                    try {
                        byte[] buffer = new byte[1024];
                        int r = 0;
                        while ((r = in.read(buffer)) > -1) {
                            out.write(buffer, 0, r);
                        }
                        zf = new ZipFile(f.getAbsoluteFile());
                        ZipEntry ze;
                        zin = new ZipInputStream(new ByteArrayInputStream(data));
                        for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                            ze = ((ZipEntry) entries.nextElement());
                            zin.getNextEntry();
                            ByteArrayOutputStream newByte = new ByteArrayOutputStream();
                            buffer = new byte[1024];
                            r = 0;
                            while ((r = zin.read(buffer)) > -1) {
                                newByte.write(buffer, 0, r);
                            }
                            String newFileName = fileName + "/" + ze.toString();
                            Reader insideZip = new Reader(newFileName, mime, newByte.toByteArray());
                            result.mergeWith(insideZip.result);
                            newByte.close();
                        }
                        zin.close();
                        zf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            out.close();
                            f.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    File f = new File(fileName);
                    if (f.isDirectory()) {
                        File[] files = f.listFiles();
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream newByte;
                        FileInputStream newIn;
                        for (File ff : files) {
                            newIn = new FileInputStream(ff);
                            newByte = new ByteArrayOutputStream();
                            int r = 0;
                            while ((r = newIn.read(buffer)) > -1) {
                                newByte.write(buffer, 0, r);
                            }
                            Reader insideDir = new Reader(ff.getName(), mime, newByte.toByteArray());
                            newByte.close();
                            newIn.close();
                            result.mergeWith(insideDir.result);
                        }
                    } else {
                        result.addWrongFile(fileName);
                    }
                    in.close();
                }
            }
        }
    }
