    public static void loadRawTransformation(String filename, double[][] transformation_x, double[][] transformation_y) {
        try {
            final FileReader fr = new FileReader(filename);
            final BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            int lineN = 1;
            StringTokenizer st = new StringTokenizer(line, "=");
            if (st.countTokens() != 2) {
                fr.close();
                IJ.write("Line " + lineN + "+: Cannot read transformation width");
                return;
            }
            st.nextToken();
            int width = Integer.valueOf(st.nextToken()).intValue();
            line = br.readLine();
            lineN++;
            st = new StringTokenizer(line, "=");
            if (st.countTokens() != 2) {
                fr.close();
                IJ.write("Line " + lineN + "+: Cannot read transformation height");
                return;
            }
            st.nextToken();
            int height = Integer.valueOf(st.nextToken()).intValue();
            line = br.readLine();
            line = br.readLine();
            lineN += 2;
            for (int i = 0; i < height; i++) {
                line = br.readLine();
                lineN++;
                st = new StringTokenizer(line);
                if (st.countTokens() != width) {
                    fr.close();
                    IJ.write("Line " + lineN + ": Cannot read enough coordinates");
                    return;
                }
                for (int j = 0; j < width; j++) transformation_x[i][j] = Double.valueOf(st.nextToken()).doubleValue();
            }
            line = br.readLine();
            line = br.readLine();
            lineN += 2;
            for (int i = 0; i < height; i++) {
                line = br.readLine();
                lineN++;
                st = new StringTokenizer(line);
                if (st.countTokens() != width) {
                    fr.close();
                    IJ.write("Line " + lineN + ": Cannot read enough coordinates");
                    return;
                }
                for (int j = 0; j < width; j++) transformation_y[i][j] = Double.valueOf(st.nextToken()).doubleValue();
            }
            fr.close();
        } catch (FileNotFoundException e) {
            IJ.error("File not found exception" + e);
            return;
        } catch (IOException e) {
            IJ.error("IOException exception" + e);
            return;
        } catch (NumberFormatException e) {
            IJ.error("Number format exception" + e);
            return;
        }
    }
