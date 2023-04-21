    public static void createGraphic(String graphicFormat, int width, int height, String treeFileName, String graphicFileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(treeFileName));
            String line = bufferedReader.readLine();
            while (line != null && line.length() == 0) {
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            boolean isNexus = (line != null && line.toUpperCase().contains("#NEXUS"));
            Reader reader = new FileReader(treeFileName);
            Map<String, Object> settings = new HashMap<String, Object>();
            ExtendedTreeViewer treeViewer = new ExtendedTreeViewer();
            ControlPalette controlPalette = new BasicControlPalette(200, BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
            FigTreePanel figTreePanel = new FigTreePanel(null, treeViewer, controlPalette);
            controlPalette.getSettings(settings);
            List<Tree> trees = new ArrayList<Tree>();
            if (isNexus) {
                FigTreeNexusImporter importer = new FigTreeNexusImporter(reader);
                trees.add(importer.importNextTree());
                while (true) {
                    try {
                        importer.findNextBlock();
                        if (importer.getNextBlockName().equalsIgnoreCase("FIGTREE")) {
                            importer.parseFigTreeBlock(settings);
                        }
                    } catch (EOFException ex) {
                        break;
                    }
                }
            } else {
                NewickImporter importer = new NewickImporter(reader, true);
                trees.add(importer.importNextTree());
            }
            if (trees.size() == 0) {
                throw new ImportException("This file contained no trees.");
            }
            treeViewer.setTrees(trees);
            controlPalette.setSettings(settings);
            treeViewer.getContentPane().setSize(width, height);
            OutputStream stream;
            if (graphicFileName != null) {
                stream = new FileOutputStream(graphicFileName);
            } else {
                stream = System.out;
            }
            Properties p = new Properties();
            VectorGraphics g;
            if (graphicFormat.equals("PDF")) {
                if (graphicFileName != null) {
                    System.out.println("Creating PDF graphic: " + graphicFileName);
                }
                g = new PDFGraphics2D(stream, new Dimension(width, height));
            } else if (graphicFormat.equals("PS")) {
                if (graphicFileName != null) {
                    System.out.println("Creating PS graphic: " + graphicFileName);
                }
                g = new PSGraphics2D(stream, new Dimension(width, height));
            } else if (graphicFormat.equals("EMF")) {
                if (graphicFileName != null) {
                    System.out.println("Creating EMF graphic: " + graphicFileName);
                }
                g = new EMFGraphics2D(stream, new Dimension(width, height));
            } else if (graphicFormat.equals("SVG")) {
                if (graphicFileName != null) {
                    System.out.println("Creating SVG graphic: " + graphicFileName);
                }
                g = new SVGGraphics2D(stream, new Dimension(width, height));
            } else if (graphicFormat.equals("SWF")) {
                if (graphicFileName != null) {
                    System.out.println("Creating SWF graphic: " + graphicFileName);
                }
                g = new SWFGraphics2D(stream, new Dimension(width, height));
            } else if (graphicFormat.equals("GIF")) {
                if (graphicFileName != null) {
                    System.out.println("Creating GIF graphic: " + graphicFileName);
                }
                g = new GIFGraphics2D(stream, new Dimension(width, height));
            } else {
                throw new RuntimeException("Unknown graphic format");
            }
            g.setProperties(p);
            g.startExport();
            treeViewer.getContentPane().print(g);
            g.endExport();
        } catch (ImportException ie) {
            throw new RuntimeException("Error writing graphic file: " + ie);
        } catch (IOException ioe) {
            throw new RuntimeException("Error writing graphic file: " + ioe);
        }
    }
