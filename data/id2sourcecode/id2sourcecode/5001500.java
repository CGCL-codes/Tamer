        public String getCommandString() {
            int v = Math.round(getVoltage() * 1000);
            int n = getChannel();
            String s = String.format("d %d %d mv\n", n, v);
            return s;
        }
