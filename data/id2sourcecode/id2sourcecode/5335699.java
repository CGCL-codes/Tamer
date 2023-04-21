    public void toDemoClass(String outputFile) {
        try {
            int c = 0;
            String line;
            String nextLine;
            String temp;
            Enumeration entries;
            BufferedReader template = new BufferedReader(new FileReader(DEMO_TEMPLATE));
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
            while ((line = template.readLine()) != null) {
                if (line.equals("//DEMOMAKER: INSERT GRAMMAR HERE!")) {
                    entries = grammar.propertyNames();
                    while (entries.hasMoreElements()) {
                        temp = entries.nextElement().toString();
                        nextLine = "      grammar.setProperty(\"" + temp + "\",\"" + grammar.getProperty(temp) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " grammar entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT INFO HERE!")) {
                    entries = info.propertyNames();
                    while (entries.hasMoreElements()) {
                        temp = entries.nextElement().toString();
                        nextLine = "      info.setProperty(\"" + temp + "\",\"" + info.getProperty(temp) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " info entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT LAYOUT HERE!")) {
                    for (int i = 0; i < layout.size(); i++) {
                        nextLine = "      layout.add(\"" + layout.get(i) + "\");";
                        output.write(nextLine);
                        output.newLine();
                        c++;
                    }
                    voice.verbose(c + " layout entries added.");
                    c = 0;
                } else if (line.equals("//DEMOMAKER: INSERT WORDLISTS HERE!")) {
                    for (int i = 0; i < wordLists.size(); i++) {
                        bcWordList tempL = (bcWordList) wordLists.get(i);
                        output.write("      tempList = new bcWordList(\"" + tempL.getFileName() + "\");");
                        output.newLine();
                        for (int j = 0; j < tempL.getNumberOfWords(); j++) {
                            nextLine = "      tempList.addWord(\"" + tempL.getItem(j).getWord() + "\",\"" + tempL.getItem(j).getPhonemes() + "\");";
                            output.write(nextLine);
                            output.newLine();
                            c++;
                        }
                        output.write("      wordLists.add(tempList);");
                        output.newLine();
                        output.newLine();
                    }
                    voice.verbose(c + " word list entries added.");
                    c = 0;
                } else {
                    output.write(line);
                    output.newLine();
                }
            }
            template.close();
            output.close();
        } catch (IOException e) {
            voice.sysout("Error: Could not create demo module - " + e.toString());
        }
        voice.sysout("bcDemoModule class created at " + outputFile + ".");
    }
