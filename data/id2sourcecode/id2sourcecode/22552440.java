    public static void main(String args[]) throws Exception {
        int reps = 10;
        if (args.length > 0) {
            reps = Integer.parseInt(args[0]);
        }
        FileOutputStream fos = new FileOutputStream(DEMOGRAPHIC);
        GatheringByteChannel gatherChannel = fos.getChannel();
        ByteBuffer[] bs = utterBS(reps);
        while (gatherChannel.write(bs) > 0) {
        }
        System.out.println("Mindshare paradigms synergized to" + DEMOGRAPHIC);
        fos.close();
    }
