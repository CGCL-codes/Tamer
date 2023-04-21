    public void changeAI(String outFile) {
        Random r = new Random();
        try {
            int min = Integer.parseInt(this.ai.min);
            int max = Integer.parseInt(this.ai.max);
            int test = r.nextInt(max);
            if (test > min) {
                return;
            } else {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                Document doc = null;
                doc = db.newDocument();
                Element root = doc.createElement("AI");
                doc.appendChild(root);
                root.setAttribute("max", this.ai.max);
                root.setAttribute("min", this.ai.min);
                Element people = doc.createElement("people");
                root.appendChild(people);
                for (int i = 0; i < 7; i++) {
                    Element chessmanNode = doc.createElement("chessman");
                    people.appendChild(chessmanNode);
                    chessmanNode.setAttribute("name", this.ai.people[i].name);
                    chessmanNode.setAttribute("self", this.ai.people[i].self);
                    chessmanNode.setAttribute("underAttack", this.ai.people[i].underAttack);
                    chessmanNode.setAttribute("canGoPosition", this.ai.people[i].canGoPosition);
                    chessmanNode.setAttribute("protected", this.ai.people[i].protectedV);
                    System.out.println("pos size : " + this.ai.people[i].goodPosition.size());
                    for (int j = 0; j < this.ai.people[i].goodPosition.size(); ++j) {
                        PosInfo temp = (PosInfo) (this.ai.people[i].goodPosition.get(j));
                        Element p = doc.createElement("goodPosition");
                        chessmanNode.appendChild(p);
                        p.setAttribute("x", temp.x);
                        p.setAttribute("y", temp.y);
                        p.setAttribute("value", temp.v);
                    }
                }
                Element computer = doc.createElement("computer");
                root.appendChild(computer);
                for (int i = 0; i < 7; i++) {
                    Element chessmanNode = doc.createElement("chessman");
                    computer.appendChild(chessmanNode);
                    chessmanNode.setAttribute("name", this.ai.computer[i].name);
                    chessmanNode.setAttribute("self", this.ai.computer[i].self);
                    chessmanNode.setAttribute("underAttack", this.ai.computer[i].underAttack);
                    chessmanNode.setAttribute("canGoPosition", this.ai.computer[i].canGoPosition);
                    chessmanNode.setAttribute("protected", this.ai.computer[i].protectedV);
                    for (int j = 0; j < this.ai.computer[i].goodPosition.size(); ++j) {
                        PosInfo temp = (PosInfo) (this.ai.computer[i].goodPosition.get(j));
                        Element p = doc.createElement("goodPosition");
                        chessmanNode.appendChild(p);
                        p.setAttribute("x", temp.x);
                        p.setAttribute("y", temp.y);
                        p.setAttribute("value", temp.v);
                    }
                }
                FileOutputStream outStream = new FileOutputStream(outFile);
                OutputStreamWriter outWriter = new OutputStreamWriter(outStream);
                ((XmlDocument) doc).write(outWriter, "gb2312");
                outWriter.close();
                outStream.close();
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
