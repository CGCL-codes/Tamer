    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("usage: FileBenchClient [dest] [port] [file] [iteration]");
            return;
        }
        InetAddress dest = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        int iteration = Integer.parseInt(args[3]);
        long start, end, etime;
        long sum = 0, ss = 0;
        for (int i = 0; i < iteration; i++) {
            start = System.currentTimeMillis();
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(dest, port));
            FileInputStream in = new FileInputStream(args[2]);
            FileChannel fchannel = in.getChannel();
            ByteBuffer buf = ByteBuffer.allocateDirect(512);
            buf.clear();
            buf.putLong(fchannel.size());
            buf.flip();
            channel.write(buf);
            fchannel.transferTo(0, fchannel.size(), channel);
            fchannel.close();
            channel.close();
            end = System.currentTimeMillis();
            etime = end - start;
            sum += etime;
            ss += etime * etime;
        }
        double mean = sum / (double) iteration / 1000.0;
        double sd = Math.sqrt(ss / (double) iteration / 1000000.0 - mean * mean);
        System.out.println("mean=" + mean + ", sd=" + sd);
    }
