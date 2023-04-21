    private static boolean generateMovieCustomerRatingBinaryFile(String completePath, String outFileName) {
        try {
            File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + outFileName);
            FileChannel outC = new FileOutputStream(outFile, true).getChannel();
            File src = new File(completePath + fSep + "training_set" + fSep);
            if (src.isDirectory()) {
                File list[] = src.listFiles();
                for (int i = 0; i < list.length; i++) {
                    String fileName = list[i].getName();
                    if (!fileName.startsWith("mv_")) continue;
                    System.out.println("Processing movie file: " + fileName);
                    BufferedReader br1 = new BufferedReader(new FileReader(completePath + fSep + "training_set" + fSep + fileName));
                    boolean endOfFile = true;
                    short movieName = 0;
                    int customer = 0;
                    byte rating = 0;
                    while (endOfFile) {
                        String line = br1.readLine();
                        if (line != null) {
                            if (line.indexOf(":") >= 0) {
                                movieName = new Short(line.substring(0, line.length() - 1)).shortValue();
                            } else {
                                StringTokenizer tokens = new StringTokenizer(line, ",");
                                if (tokens.countTokens() == 3) {
                                    customer = new Integer(tokens.nextToken()).intValue();
                                    rating = new Integer(tokens.nextToken()).byteValue();
                                    ByteBuffer outBuf = ByteBuffer.allocate(7);
                                    outBuf.putShort(movieName);
                                    outBuf.putInt(customer);
                                    outBuf.put(rating);
                                    outBuf.flip();
                                    outC.write(outBuf);
                                } else {
                                    outC.close();
                                    return false;
                                }
                            }
                        } else endOfFile = false;
                    }
                    br1.close();
                }
                outC.close();
            } else {
                System.out.println("Incorrect path provided. Please provide the complete path to the training_set data files ");
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
