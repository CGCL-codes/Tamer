    @Override
    public String getSampleCode() {
        return "require \"socket\"" + PMD.EOL + "" + PMD.EOL + "gs = TCPServer.open(0)" + PMD.EOL + "addr = gs.addr" + PMD.EOL + "addr.shift" + PMD.EOL + "" + PMD.EOL + "while true" + PMD.EOL + "  ns = gs.accept" + PMD.EOL + "  print(ns, \" is accepted\")" + PMD.EOL + "  Thread.start do" + PMD.EOL + "    s = ns                      # save to dynamic variable" + PMD.EOL + "    while s.gets" + PMD.EOL + "      s.write($_)" + PMD.EOL + "    end" + PMD.EOL + "    print(s, \" is " + PMD.EOL + "               gone" + PMD.EOL + "                       and" + PMD.EOL + "                               dead\")" + PMD.EOL + "    s.close" + PMD.EOL + "  end" + PMD.EOL + "end" + PMD.EOL;
    }
