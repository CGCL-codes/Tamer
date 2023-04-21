    public void run() {
        int num = 0;
        while (((!isFinished()) && (num < this.mCapacity)) || !this.mTaskQueue.isEmpty()) {
            try {
                ProcessingResult pr = this.mTaskQueue.takeFirst();
                Logger log = pr.getLog();
                if (pr.getError() == null) {
                    Point vertex = pr.getVertex();
                    Point left = pr.getLeftCalibration();
                    Point right = pr.getRightCalibration();
                    File inputFile = pr.getFile();
                    BufferedImage inputImage = pr.getInputImage();
                    Function f = pr.getFunction();
                    log.finest("Berechne das Outputbild");
                    BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = outputImage.getGraphics();
                    g.drawImage(inputImage, 0, 0, null);
                    g.setColor(Color.red);
                    List<Point> detectedPoints = pr.getPoints();
                    String[][] pointValues = new String[detectedPoints.size() + 2][2];
                    g.setColor(new Color(104, 255, 32));
                    int i = 2;
                    int vertexX = vertex.getCor()[0];
                    int leftY = left.getCor()[1];
                    pointValues[0][0] = NumberFormat.getNumberInstance().format(left.getCor()[0] - vertexX);
                    pointValues[0][1] = NumberFormat.getNumberInstance().format(0);
                    pointValues[1][0] = NumberFormat.getNumberInstance().format(right.getCor()[0] - vertexX);
                    pointValues[1][1] = NumberFormat.getNumberInstance().format(0);
                    for (Point p : detectedPoints) {
                        int x2 = p.getCor()[0];
                        int y2 = p.getCor()[1];
                        g.fillRect(x2 - 1, y2 - 1, 3, 3);
                        pointValues[i][0] = NumberFormat.getNumberInstance().format(x2 - vertexX);
                        pointValues[i][1] = NumberFormat.getNumberInstance().format(Math.abs(leftY - y2));
                        i++;
                    }
                    g.setColor(Color.RED);
                    for (int x = pr.getMinXmaxY(); x < pr.getMaxXmaxY(); x += POINT_GAP) {
                        double y = f.function(x);
                        g.drawOval(x - 2, ((int) y) - 2, 4, 4);
                    }
                    int calibY = (left.getCor()[1] + right.getCor()[1]) / 2;
                    int leftBound = pr.getLeftBound();
                    int rightBound = pr.getRightBound();
                    g.fillRect(leftBound, calibY, rightBound - leftBound + 3, 3);
                    g.fillRect(leftBound, 0, 3, calibY);
                    g.fillRect(rightBound, 0, 3, calibY);
                    g.setColor(Color.white);
                    StringBuffer drawString = new StringBuffer("f(x) = ");
                    drawString.append(f.toString());
                    g.drawString(drawString.toString(), 100, 100);
                    g.drawString("Surface = " + pr.getSurface(), 100, 120);
                    g.drawString("Volume = " + pr.getVolume(), 100, 140);
                    g.setColor(Color.blue);
                    g.drawOval(vertex.getCor()[0] - 5, vertex.getCor()[1] - 5, 10, 10);
                    g.dispose();
                    log.finest("Berechnung abgeschlossen");
                    File output = FileHandling.formOutputImageFile(inputFile, mProject.getProjectDirectory(), count);
                    ConcurrentBufferedImageWriter cbiw = new ConcurrentBufferedImageWriter(output, "png");
                    log.finest("Speichere Bild");
                    this.mWriterThreads.add(cbiw.write(outputImage));
                    pr.setOutputImage(output);
                    double fac = pr.getUnitValue() / Math.sqrt((pr.getMeasurePoints()[1].getCor()[0] - pr.getMeasurePoints()[0].getCor()[0]) * (pr.getMeasurePoints()[1].getCor()[0] - pr.getMeasurePoints()[0].getCor()[0]) + (pr.getMeasurePoints()[1].getCor()[1] - pr.getMeasurePoints()[0].getCor()[1]) * (pr.getMeasurePoints()[1].getCor()[1] - pr.getMeasurePoints()[0].getCor()[1]));
                    String[] header = { Flames2D.formMeterOutput(fac, mProject.getUnit()) + "/px" };
                    OutputWriter pointList = new OutputWriter(header, pointValues);
                    File pointListOutput = new File(this.mProject.getProjectDirectory().getAbsolutePath() + File.separator + count + "_" + inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")) + "_pt.txt");
                    log.finest("Speichere Punktmenge in Datei");
                    pointList.write(pointListOutput);
                    String[] fHeader = { "Function:", pr.getFunction().toString() };
                    double[] coeffs = pr.getFunction().getCoefficients();
                    String[][] fCoeffs = new String[coeffs.length][1];
                    i = 0;
                    for (String[] coeff : fCoeffs) {
                        coeff[0] = String.valueOf(coeffs[i]);
                        i++;
                    }
                    OutputWriter functionCoeffsWriter = new OutputWriter(fHeader, fCoeffs, String.valueOf(pr.getSurface()) + mProject.getUnit() + "²" + "\nVolume: " + pr.getVolume() + mProject.getUnit() + "³");
                    File functionCoeffsFile = new File(this.mProject.getProjectDirectory().getAbsolutePath() + File.separator + count + "_" + inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")) + "_fct.txt");
                    log.finest("Speichere Funktion in Datei");
                    functionCoeffsWriter.write(functionCoeffsFile);
                    count++;
                } else {
                    log.warning("Ausgabedateien wurden nicht geschrieben!");
                }
            } catch (InterruptedException e) {
                LogManager.logWarning(this.getClass().getSimpleName(), e);
            } catch (IOException io) {
                LogManager.logWarning(this.getClass().getSimpleName(), io);
            } finally {
                num++;
            }
        }
        try {
            for (Thread t : this.mWriterThreads) {
                t.join();
            }
        } catch (InterruptedException e) {
            LogManager.logWarning(this.getClass().getSimpleName(), e);
        }
    }
