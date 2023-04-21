    public static void main(String[] args) throws IOException {
        Movie video = MovieCreator.build(new RandomAccessFile("/home/sannies/scm/svn/mp4parser-release/output.fmp4", "rw").getChannel());
        Iterator<ByteBuffer> iter = video.getTracks().get(0).getSamples().subList(55, 65).iterator();
        for (int i = 55; i < 65; i++) {
            if (video.getTracks().get(0).getSamples().get(i).equals(iter.next())) {
                System.err.println(i + ": " + video.getTracks().get(0).getSamples().get(i).limit());
            } else {
                System.err.println("errpor");
            }
        }
    }
