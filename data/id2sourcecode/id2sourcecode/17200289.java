    private void checkArguments(Getopt getopt) {
        if (getopt.getNumCmdLineArguments() == 0 && getopt.getNumCmdLineOptions() == 0) {
            System.out.println("GetoptExample: missing argument\n" + "\n" + "Usage: java huf.misc.getopt.example.GetoptExample [OPTIONS] [arguments]\n" + "\n" + "Try 'java huf.misc.getopt.example.GetoptExample --help' for more options.");
            System.exit(0);
        }
        if (getopt.isSet("h")) {
            System.out.println("HUF Getopt Example 1.0, an example apppliaction\n" + "\n" + "This program serves no useful purpose and demonstrates usage of HUF's Getopt class.\n" + "\n" + "Usage: java huf.misc.getopt.example.GetoptExample [OPTIONS] [arguments]\n" + "\n" + "Options:\n" + "  -h,   --help               print help message (you're reading it)\n" + "  -V                         print version and copyright information\n" + "  -v,   --verbose            be more verbose; you can set it to three levels:\n" + "                             low, med, or high, eg. --verbose med\n" + "  -if,  --input-file FILE    read input from specified file instead of standard input\n" + "  -of,  --output-file FILE   write input to specified file instead of default \"output.txt\"\n" + "\n");
            System.exit(0);
        }
        if (getopt.isSet("V")) {
            System.out.println("HUF Getopt Example 1.0, an example apppliaction\n" + "\n" + "Copyright (c) 2003 Max Gilead <max.gilead@gmail.com>\n" + "\n" + "Distributed under terms of GNU General Public License.\n" + "This program is distributed in the hope that it will be useful,\n" + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See COPYING\n" + "file or http://www.gnu.org/licenses/gpl.txt for more details.\n" + "\n");
            System.exit(0);
        }
    }
