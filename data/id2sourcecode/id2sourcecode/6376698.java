        public void Install(int port, IO_WriteHandler handler, int mask, int range) {
            if (!installed) {
                installed = true;
                m_port = port;
                m_mask = mask;
                m_range = range;
                IO_RegisterWriteHandler(port, handler, mask, range);
            } else Log.exit("IO_writeHandler allready installed port " + Integer.toString(port, 16));
        }
