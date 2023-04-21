        void doMovements() {
            Random r = new Random();
            int x = 0;
            int y = 0;
            int v = 0;
            executeAndWait("c", rm.out);
            while (rm.keepOn) {
                x = r.nextInt(XMAX - XMIN + 1) + XMIN;
                y = r.nextInt(YMAX - YMIN + 1) + YMIN;
                v = r.nextInt(VMAX + 1);
                v = VMAX;
                String command = "x" + x + "y" + y + "v" + v + "m\r";
                System.out.printf("   x: %5d     y: %5d \n", x, y);
                executeAndWait(command, rm.out);
            }
        }
