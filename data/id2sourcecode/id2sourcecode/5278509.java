    public static String updateSVGFile(String fname) {
        try {
            String tmp = GarminConfiguration.getTmpFile();
            if (tmp == null) tmp = fname;
            File f = new File(tmp);
            if (f.exists() && !(fname.startsWith("jar"))) f.delete();
            Date d = new Date();
            int hc = d.hashCode();
            if (hc < 0) hc = 0 - hc;
            fname = System.getProperty("user.dir") + System.getProperty("file.separator") + Integer.toString(hc) + ".svg";
            File ftmp = new File(fname);
            ftmp.createNewFile();
            GarminConfiguration.setTmpFile(fname);
            PrintWriter fout = new PrintWriter(new FileOutputStream(ftmp));
            URL url = new URL("jar:file:net.aetherial.gis.garmin.jar!/net/aetherial/gis/garmin/defaultMap.svg");
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            BufferedReader bin = new BufferedReader(new InputStreamReader(jarConnection.getInputStream()));
            String line = null;
            for (line = bin.readLine(); !(line.startsWith("</svg>")) && line != null; line = bin.readLine()) {
                fout.println(line);
            }
            NodeList nl = root.getElementsByTagName("waypoint");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                String name = "no name";
                String x = "0";
                String y = "0";
                NodeList nln = e.getElementsByTagName("gml:name");
                for (int j = 0; nln != null && j < nln.getLength(); j++) {
                    if (nln.item(j) != null) {
                        NodeList nlc = nln.item(j).getChildNodes();
                        name = nlc.item(0).getNodeValue();
                    }
                }
                nln = e.getElementsByTagName("X");
                for (int j = 0; nln != null && j < nln.getLength(); j++) {
                    if (nln.item(j) != null) {
                        NodeList nlc = nln.item(j).getChildNodes();
                        x = nlc.item(0).getNodeValue();
                    }
                }
                nln = e.getElementsByTagName("Y");
                for (int j = 0; nln != null && j < nln.getLength(); j++) {
                    if (nln.item(j) != null) {
                        NodeList nlc = nln.item(j).getChildNodes();
                        y = nlc.item(0).getNodeValue();
                    }
                }
                fout.println("<circle style=\"fill:gray;stroke:none;opacity:0.25\" cx=\"" + x + "\" cy=\"" + y + "\" r=\"1\"/>");
                fout.println("<circle style=\"fill:black;stroke:none;opacity:1.0\" cx=\"" + x + "\" cy=\"" + y + "\" r=\"0.001\"/>");
                x = Double.toString(Double.parseDouble(x) + 0.0015);
                y = Double.toString(Double.parseDouble(y) + 0.0015);
                fout.println("<text x=\"" + x + "\" y=\"" + y + "\" style=\"font-size:0.01px;font-family:san-serif;\">" + name + "</text>");
            }
            nl = root.getElementsByTagName("track");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                String name = "no name";
                String path = "M ";
                NodeList nln = e.getElementsByTagName("gml:name");
                for (int j = 0; nln != null && j < nln.getLength(); j++) {
                    if (nln.item(j) != null) {
                        NodeList nlc = nln.item(j).getChildNodes();
                        name = nlc.item(0).getNodeValue();
                    }
                }
                nln = e.getElementsByTagName("gml:coord");
                String text = "";
                for (int j = 0; nln != null && j < nln.getLength(); j++) {
                    String x = "0";
                    String y = "0";
                    NodeList nln2 = ((Element) nln.item(j)).getElementsByTagName("X");
                    for (int k = 0; nln2 != null && k < nln2.getLength(); k++) {
                        if (nln2.item(k) != null) {
                            NodeList nlc = nln2.item(k).getChildNodes();
                            x = nlc.item(0).getNodeValue();
                        }
                    }
                    nln2 = ((Element) nln.item(j)).getElementsByTagName("Y");
                    for (int k = 0; nln2 != null && k < nln2.getLength(); k++) {
                        if (nln2.item(k) != null) {
                            NodeList nlc = nln2.item(k).getChildNodes();
                            y = nlc.item(0).getNodeValue();
                        }
                    }
                    if (j != (nln.getLength() - 1)) path = path + x + " " + y + " L "; else path = path + x + " " + y;
                    if (j == 0) {
                        fout.println("<circle style=\"fill:gray;stroke:none;opacity:0.25\" cx=\"" + x + "\" cy=\"" + y + "\" r=\"1\"/>");
                        text = "<text x=\"" + x + "\" y=\"" + y + "\" style=\"font-size:0.25;font-family:san-serif;\">" + name + "</text>";
                    }
                }
                fout.println("<path d=\"" + path + "\" style=\"stroke:red;stroke-width:0.0100;fill:none;opacity:0.25;\"/>");
                fout.println("<path d=\"" + path + "\" style=\"stroke:black;stroke-width:0.00100;fill:none;\"/>");
                fout.println(text);
            }
            fout.println("</svg>");
            fout.close();
            bin.close();
            fname = "file:///" + System.getProperty("user.dir") + System.getProperty("file.separator") + Integer.toString(hc) + ".svg";
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return fname;
    }
