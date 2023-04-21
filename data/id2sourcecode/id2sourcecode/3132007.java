    public static void openWithFormat(Vector<String> format, MapEditorGUI editor, File f) {
        Vector<String> problems = new Vector<String>();
        boolean lay1 = false, lay2 = false, lay3 = false;
        try {
            Scanner scan = new Scanner(f);
            for (int i = 0; i < format.size(); i++) {
                String s = format.get(i);
                System.out.println(s);
                if (s.equals("Start Location")) {
                    String ss = scan.nextLine();
                    if (ss.equals("")) problems.add("Starting point undefined."); else {
                        StringTokenizer token = new StringTokenizer(ss);
                        int x = Integer.parseInt(token.nextToken());
                        int y = Integer.parseInt(token.nextToken());
                        editor.mapPane.startLocation = new Point(x, y);
                    }
                } else if (s.equals("Map Image")) {
                    editor.mapPane.setMapImage(ImageUtil.getImage(editor.getClass().getResource(scan.nextLine())));
                } else if (s.equals("Map Music")) {
                    editor.mapPane.mscPath = scan.nextLine();
                    if (editor.mapPane.mscPath.equals("")) problems.add("Map music undefined.");
                } else if (s.equals("Layer 1 Data")) {
                    editor.mapPane.lay1 = fillLayer(scan);
                    lay1 = true;
                } else if (s.equals("Layer 2 Data")) {
                    editor.mapPane.lay2 = fillLayer(scan);
                    lay2 = true;
                } else if (s.equals("Layer 3 Data")) {
                    editor.mapPane.lay3 = fillLayer(scan);
                    lay3 = true;
                } else if (s.equals("Map Dimension in tiles")) {
                    editor.mapPane.setMapSize(scan.nextInt(), scan.nextInt());
                } else if (scan.hasNextLine()) {
                    System.out.println("Ignore " + scan.nextLine());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(editor, "Irresolvable exception opening map " + f.getAbsolutePath() + "\nFile's format might differ from the current map loading format." + "\nDetails: " + e, "Map Format Exception", JOptionPane.WARNING_MESSAGE);
        }
        if (!lay1) clearLayer(editor.mapPane.lay1);
        if (!lay2) clearLayer(editor.mapPane.lay2);
        if (!lay3) clearLayer(editor.mapPane.lay3);
        fitMap(editor.mapPane);
        if (problems.size() > 0) {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < problems.size(); i++) buff.append(problems.get(i) + "\n");
            JOptionPane.showMessageDialog(editor, buff.toString(), f.getName() + " details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
