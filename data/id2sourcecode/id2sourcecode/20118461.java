        public User(String name) throws Exception {
            this.name = name;
            channel = rtpManager.getChannel();
            channel.bind();
            channel.addFormat(rtpFormat);
            gen = new Generator("generator[" + name + "]", scheduler);
            det = new Detector("detector[" + name + "]", scheduler);
            PipeImpl rxPipe = new PipeImpl();
            PipeImpl txPipe = new PipeImpl();
            rxPipe.connect(det);
            rxPipe.connect(channel.getInput());
            txPipe.connect(gen);
            txPipe.connect(channel.getOutput());
        }
