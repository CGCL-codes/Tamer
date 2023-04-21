    public void print(Hashtable catInfo, String type) {
        String leftMargin = "          ";
        String paraSpace = "    ";
        String spaceAfterClassno = countSpaces(9 - (StringProcessor.getInstance().verifyString(catInfo.get("classNumber")).length()));
        String spaceAfterBookno = countSpaces(9 - (StringProcessor.getInstance().verifyString(catInfo.get("bookNumber")).length()));
        String spaceAfterAccno = "   ";
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
            correctout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(correctFile, true), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));
            int totalLineNo = 0;
            out.newLine();
            if (type.equals("otherAuthorCard")) {
                String author = (String) ((Vector) catInfo.get("addedEntries")).get(authorCount);
                authorCount++;
                out.write(paraSpace + author);
                out.newLine();
                out.newLine();
                System.out.println("printing otherAuthorCard");
            }
            if (type.equals("seriesCard")) {
                String seriesName = (String) ((Vector) catInfo.get("series")).get(seriesCount);
                seriesCount++;
                out.write(paraSpace + seriesName);
                out.newLine();
                out.newLine();
                System.out.println("printing Series card");
            }
            if (type.equals("subjectCard")) {
                String subjectName = (String) ((Vector) catInfo.get("subjects")).get(subjectsCount);
                subjectsCount++;
                out.write(paraSpace + subjectName.toUpperCase());
                out.newLine();
                out.newLine();
            }
            if (type.equals("titleCard")) {
                out.write(paraSpace + StringProcessor.getInstance().verifyString(catInfo.get("TITLE")));
                out.newLine();
                out.newLine();
            }
            out.write(StringProcessor.getInstance().verifyString(catInfo.get("MAIN_ENTRY")));
            out.newLine();
            out.newLine();
            out.write(paraSpace + StringProcessor.getInstance().verifyString(catInfo.get("Title_SOR")));
            String parallelTitle = StringProcessor.getInstance().verifyString(catInfo.get("parallel_title"));
            if (!parallelTitle.equals("")) out.write("=" + parallelTitle);
            String edition = StringProcessor.getInstance().verifyString(catInfo.get("edition"));
            if (!edition.equals("")) out.write(". - " + edition);
            String publisher = StringProcessor.getInstance().verifyString(catInfo.get("publisher"));
            if (!publisher.equals("")) out.write(". - " + publisher);
            out.newLine();
            out.newLine();
            out.write(paraSpace + StringProcessor.getInstance().verifyString(catInfo.get("PHYSICAL_DESCRIPTION")));
            Enumeration en = ((Vector) catInfo.get("series")).elements();
            int i = 0;
            if ((((Vector) catInfo.get("series")).size()) != 0) {
                out.write("-(");
                while (en.hasMoreElements()) {
                    i++;
                    String series = (String) en.nextElement();
                    out.write("" + i + "." + series + " ");
                }
                out.write(")");
            }
            out.newLine();
            out.newLine();
            if (type.equals("mainCard") || type.equals("classNoCard")) {
                out.write(paraSpace);
                en = ((Vector) catInfo.get("subjects")).elements();
                i = 0;
                while (en.hasMoreElements()) {
                    i++;
                    String sub = (String) en.nextElement();
                    out.write("" + i + "." + sub.toUpperCase() + " ");
                }
                out.write(" ");
                Vector v = (Vector) catInfo.get("addedEntries");
                en = v.elements();
                i = 0;
                String roman = "";
                while (en.hasMoreElements()) {
                    i++;
                    roman = covertRoman(i);
                    String addenentry = (String) en.nextElement();
                    out.write(roman + "." + addenentry + " ");
                }
                i++;
                roman = covertRoman(i);
                out.write(roman + ".Title");
            }
            out.close();
            int width = 45;
            int height = 11;
            String nextLine = null;
            int lineNo = 0;
            int cardNo = 1;
            en = ((Vector) catInfo.get("accessionNo")).elements();
            i = 0;
            int noOfAccno = 0;
            String accno[] = new String[10];
            while (en.hasMoreElements() && noOfAccno < 10) {
                noOfAccno++;
                accno[i] = StringProcessor.getInstance().verifyString(en.nextElement());
                i++;
            }
            spaceAfterAccno = countSpaces(9 - (StringProcessor.getInstance().verifyString(accno[0]).length()));
            while ((nextLine = in.readLine()) != null) {
                if (nextLine.length() > width) {
                    int times = nextLine.length() / width + 1;
                    int startPos = 0;
                    int endPos = 0;
                    for (i = 0; i < times; i++) {
                        System.out.println("inside for loop");
                        if (cardNo != 1) correctout.write(leftMargin);
                        if (lineNo == 1 && cardNo == 1) correctout.write(" " + StringProcessor.getInstance().verifyString(catInfo.get("classNumber")) + spaceAfterClassno);
                        if (lineNo == 2 && cardNo == 1) correctout.write(" " + StringProcessor.getInstance().verifyString(catInfo.get("bookNumber")) + spaceAfterBookno);
                        if (lineNo > 7 && lineNo < noOfAccno + 8 && cardNo == 1) correctout.write(" " + accno[lineNo - 8] + spaceAfterAccno);
                        if ((lineNo == 3 || lineNo == 4 || lineNo == 5 || lineNo == 6 || lineNo == 7 || lineNo >= noOfAccno + 8) && cardNo == 1) correctout.write(leftMargin);
                        String newLine = "";
                        System.out.println("i=" + i);
                        if (i == 0) startPos = 0; else startPos = endPos;
                        endPos = startPos + width;
                        System.out.println("endPos=" + endPos);
                        if (i != times - 1) {
                            newLine = nextLine.substring(startPos, endPos);
                            endPos = newLine.lastIndexOf(" ") + startPos + 1;
                            if (endPos > width - 15) newLine = nextLine.substring(startPos, endPos); else {
                                endPos = startPos + width;
                                newLine = nextLine.substring(startPos, endPos) + "-";
                            }
                        }
                        if (i == times - 1) {
                            newLine = nextLine.substring(startPos);
                            if (newLine.length() > width) {
                                newLine = nextLine.substring(startPos, endPos);
                                times = times + 1;
                                endPos = newLine.lastIndexOf(" ") + startPos + 1;
                                if (endPos > width - 20) newLine = nextLine.substring(startPos, endPos); else {
                                    endPos = startPos + width;
                                    newLine = nextLine.substring(startPos, endPos) + "-";
                                }
                            }
                        }
                        if (lineNo > height && i != times - 1 && !newLine.trim().equals("")) {
                            System.out.println("line no =" + lineNo);
                            cardNo++;
                            totalLineNo = cardNo * 17;
                            correctout.newLine();
                            correctout.newLine();
                            correctout.newLine();
                            correctout.newLine();
                            correctout.write("                                            " + "Contd:");
                            correctout.newLine();
                            correctout.newLine();
                            String mainEntry = StringProcessor.getInstance().verifyString(catInfo.get("MAIN_ENTRY"));
                            int entryLength = mainEntry.length();
                            int times2 = entryLength / width + 1;
                            int start = 0, end = 0;
                            String subEntry = "";
                            for (int j = 0; j < times2; j++) {
                                start = end;
                                end = start + width;
                                if (j != times2 - 1) {
                                    subEntry = mainEntry.substring(start, end);
                                    end = subEntry.lastIndexOf(" ", end) + start + 1;
                                    subEntry = mainEntry.substring(start, end);
                                } else subEntry = mainEntry.substring(start);
                                correctout.write(leftMargin + subEntry);
                                correctout.newLine();
                                lineNo++;
                            }
                            lineNo = 2;
                            correctout.newLine();
                            lineNo++;
                            correctout.newLine();
                            lineNo++;
                            String title = StringProcessor.getInstance().verifyString(catInfo.get("Title_SOR")) + "(Card:" + cardNo + ")";
                            int titleLength = title.length();
                            int times1 = titleLength / width + 1;
                            start = 0;
                            end = 0;
                            String subTitle = "";
                            for (int j = 0; j < times1; j++) {
                                start = end;
                                if (j == 0) end = start + width - 4; else end = start + width;
                                if (j != times1 - 1) {
                                    subTitle = title.substring(start, end);
                                    end = subTitle.lastIndexOf(" ", end) + start + 1;
                                    if (end > width - 20) subTitle = title.substring(start, end); else {
                                        end = start + width - 4;
                                        subTitle = title.substring(start, end);
                                    }
                                } else subTitle = title.substring(start);
                                if (j == 0) correctout.write(paraSpace);
                                correctout.write(leftMargin + subTitle);
                                correctout.newLine();
                                lineNo++;
                            }
                            correctout.newLine();
                            lineNo++;
                            correctout.write(leftMargin);
                        }
                        correctout.write(newLine);
                        correctout.newLine();
                        lineNo++;
                    }
                } else {
                    if (cardNo != 1) correctout.write(leftMargin);
                    if (lineNo == 1 && cardNo == 1) correctout.write(" " + StringProcessor.getInstance().verifyString(catInfo.get("classNumber")) + spaceAfterClassno);
                    if (lineNo == 2 && cardNo == 1) correctout.write(" " + StringProcessor.getInstance().verifyString(catInfo.get("bookNumber")) + spaceAfterBookno);
                    if (lineNo > 7 && lineNo < noOfAccno + 8 && cardNo == 1) correctout.write(" " + accno[lineNo - 8] + spaceAfterAccno);
                    if ((lineNo == 3 || lineNo == 4 || lineNo == 5 || lineNo == 6 || lineNo == 7 || lineNo >= noOfAccno + 8) && cardNo == 1) correctout.write(leftMargin);
                    if (lineNo > height && !nextLine.trim().equals("")) {
                        cardNo++;
                        totalLineNo = cardNo * 17;
                        correctout.newLine();
                        correctout.newLine();
                        correctout.newLine();
                        correctout.newLine();
                        correctout.write("                                            " + "Contd:");
                        correctout.newLine();
                        correctout.newLine();
                        String mainEntry = StringProcessor.getInstance().verifyString(catInfo.get("MAIN_ENTRY"));
                        int entryLength = mainEntry.length();
                        int times2 = entryLength / width + 1;
                        int start = 0, end = 0;
                        String subEntry = "";
                        for (int j = 0; j < times2; j++) {
                            start = end;
                            end = start + width;
                            if (j != times2 - 1) {
                                subEntry = mainEntry.substring(start, end);
                                end = subEntry.lastIndexOf(" ", end) + start + 1;
                                subEntry = mainEntry.substring(start, end);
                            } else subEntry = mainEntry.substring(start);
                            correctout.write(leftMargin + subEntry);
                            correctout.newLine();
                            lineNo++;
                        }
                        lineNo = 2;
                        correctout.newLine();
                        lineNo++;
                        correctout.newLine();
                        lineNo++;
                        String title = StringProcessor.getInstance().verifyString(catInfo.get("Title_SOR")) + "(Card:" + cardNo + ")";
                        int titleLength = title.length();
                        int times1 = titleLength / width + 1;
                        start = 0;
                        end = 0;
                        String subTitle = "";
                        for (int j = 0; j < times1; j++) {
                            start = end;
                            if (j == 0) end = start + width - 4; else end = start + width;
                            if (j != times1 - 1) {
                                subTitle = title.substring(start, end);
                                end = subTitle.lastIndexOf(" ", end) + start + 1;
                                subTitle = title.substring(start, end);
                            } else subTitle = title.substring(start);
                            if (j == 0) correctout.write(paraSpace);
                            correctout.write(leftMargin + subTitle);
                            correctout.newLine();
                            lineNo++;
                        }
                        correctout.newLine();
                        lineNo++;
                        correctout.write(leftMargin);
                    }
                    correctout.write(nextLine);
                    correctout.newLine();
                    lineNo++;
                }
            }
            if (lineNo < noOfAccno + 7 && cardNo == 1) {
                System.out.println("inside the last if loop");
                System.out.println("line no=" + lineNo);
                while (lineNo < noOfAccno + 8) {
                    if (lineNo > 7 && lineNo < noOfAccno + 8 && cardNo == 1) {
                        correctout.write(" " + accno[lineNo - 8] + spaceAfterAccno);
                        correctout.newLine();
                    }
                    lineNo++;
                }
            }
            int remainingLines = 18 - lineNo;
            for (i = 0; i < remainingLines; i++) correctout.newLine();
            in.close();
            correctout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
