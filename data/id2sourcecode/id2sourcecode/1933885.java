    public static void main(String[] args) throws IOException, InterruptedException {
        if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
            logger.info("必须输入第一个参数");
            return;
        }
        String fileDir = args[0];
        File dir = new File(fileDir);
        if (!dir.isDirectory()) {
            logger.info("输入的参数不是合法目录");
            return;
        }
        File[] mkvFiles = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".mkv");
            }
        });
        File pmpDir = new File(fileDir + (fileDir.endsWith("\\") ? "pmp" : "\\pmp"));
        File srtDir = new File(fileDir + (fileDir.endsWith("\\") ? "srt" : "\\srt"));
        pmpDir.mkdir();
        srtDir.mkdir();
        StringBuffer cmd = new StringBuffer();
        for (File mkvFile : mkvFiles) {
            String name = mkvFile.getName();
            cmd.append("mkvextract tracks \"").append(dir.getAbsolutePath() + "\\" + name).append("\" 3:\"").append(srtDir.getAbsolutePath() + "\\" + name.substring(0, name.length() - 4)).append(".ass\"").append("\r\n");
        }
        logger.info(execute(createBatFile(srtDir, "mkv2ass.bat", cmd.toString())));
        File[] assFiles = srtDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".ass");
            }
        });
        cmd.setLength(0);
        for (File assFile : assFiles) {
            if (isASS(assFile)) {
                cmd.append("ass2srt \"").append(assFile.getAbsolutePath()).append("\"\r\n");
            } else {
                assFile.renameTo(new File(assFile.getAbsolutePath().substring(0, assFile.getAbsolutePath().length() - 4) + ".srt"));
            }
        }
        logger.info(execute(createBatFile(srtDir, "ass2srtbat.bat", cmd.toString())));
        cmd.setLength(0);
        for (File mkvFile : mkvFiles) {
            String name = mkvFile.getName();
            cmd.append("start /b /wait /low mencoder.exe  ").append("-ofps 23.976 -vf harddup,scale=480:272 -ovc x264 -ffourcc H264 -x264encopts crf=24:threads=2:pictiming:nopsnr:nossim -srate 44100  -af volnorm -oac faac -faacopts br=112:mpeg=4:object=2  \"").append(mkvFile.getAbsolutePath()).append("\" -o video.avi 2>2.txt");
            cmd.append("\r\n\r\n");
            cmd.append("start /b /wait /low mencoder.exe -oac copy -ovc copy -of rawaudio video.avi -o audio.aac\r\n");
            cmd.append("pmp_muxer_avc -v video.avi -a audio.aac -s 1000 -r 23976 -d 1 -o \"").append(pmpDir.getAbsolutePath() + "\\" + name.substring(0, name.length() - 4)).append(".pmp\"\r\n");
            cmd.append("del video.avi\r\n");
            cmd.append("del audio.*\r\n");
            cmd.append("del *.log\r\n");
            cmd.append("End\r\n\r\n\r\n");
        }
        cmd.append("del *.txt\r\n");
        createBatFile(pmpDir, "mkv2pmp.bat", cmd.toString());
        cmd.setLength(0);
        for (File mkvFile : mkvFiles) {
            String name = mkvFile.getName();
            cmd.append("mkvextract tracks \"").append(dir.getAbsolutePath() + "\\" + name).append("\" 1:\"").append(pmpDir.getAbsolutePath() + "\\" + name.substring(0, name.length() - 4)).append(".avi\"").append("\r\n");
            cmd.append("mkvextract tracks \"").append(dir.getAbsolutePath() + "\\" + name).append("\" 2:\"").append(pmpDir.getAbsolutePath() + "\\" + name.substring(0, name.length() - 4)).append(".ac3\"").append("\r\n");
        }
        createBatFile(pmpDir, "mkv2avi.bat", cmd.toString());
    }
