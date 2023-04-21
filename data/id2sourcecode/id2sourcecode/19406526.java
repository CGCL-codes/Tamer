        void doMovements() {
            Random r = new Random();
            int x = 0;
            int y = 0;
            int v = 0;
            String retstr = new String(executeAndGetResponse("c", rm.out, rm.in));
            System.out.println(retstr);
            while (rm.keepOn) {
                while (isInMotion(rm.out, rm.in)) {
                    try {
                        Thread.sleep(1200);
                        System.out.print('.');
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println();
                x = r.nextInt(XMAX - XMIN + 1) + XMIN;
                y = r.nextInt(YMAX - YMIN + 1) + YMIN;
                v = r.nextInt(VMAX + 1);
                System.out.printf("   x: %5d     y: %5d     v: %2d\n", x, y, v);
                retstr = new String(executeAndGetResponse("x" + x, rm.out, rm.in));
                System.out.println(retstr);
                retstr = new String(executeAndGetResponse("y" + y, rm.out, rm.in));
                System.out.println(retstr);
                retstr = new String(executeAndGetResponse("v" + v, rm.out, rm.in));
                System.out.println(retstr);
                retstr = new String(executeAndGetResponse("m", rm.out, rm.in));
                System.out.println(retstr);
            }
        }
