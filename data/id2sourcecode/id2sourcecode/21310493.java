    public byte[] computeDescriptor(byte[] shape) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            File inFile = new File(tmpDir.getAbsolutePath() + File.separator + "din.off");
            fos = new FileOutputStream(inFile);
            fos.write(shape);
            fos.close();
            String outPrefix = tmpDir.getAbsolutePath() + File.separator + "dout";
            String cmd = String.format("%s -bbr %f -thif %d -thff %d -mr %f -opc %d %s %s", segmentExecutable.getAbsolutePath(), seg_bbr.getValue(), seg_thif.getValue(), seg_thff.getValue(), seg_mr.getValue(), seg_opc.getValue(), inFile.getPath(), outPrefix);
            Process proc = Runtime.getRuntime().exec(cmd, null, tmpDir.getAbsoluteFile());
            proc.waitFor();
            File resultFile = new File(outPrefix + "_seg.off");
            if (!resultFile.exists()) {
                System.err.println("Executable has not generated any output file!");
                return null;
            }
            fis = new FileInputStream(resultFile);
            long resultSize = fis.getChannel().size();
            byte[] result = new byte[(int) resultSize];
            fis.read(result);
            fis.close();
            return result;
        } catch (InterruptedException ex) {
            System.err.println("Could not compute Descriptor: Error when running executable.");
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Could not compute Descriptor: IO error.");
            ex.printStackTrace(System.err);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SegMatchExeWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
