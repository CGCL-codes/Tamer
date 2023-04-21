        private void printTest(SegmentPushArgsDialog test) {
            if (test.cancled()) {
                System.out.println("");
                System.out.println("Test cancled.");
            }
            System.out.println("");
            System.out.print("Segment Name: " + test.getSegmentName());
            System.out.print(";  Server: " + test.getHost() + ":" + test.getPort());
            System.out.print(";  Source/Channel: " + test.getSource() + "/" + test.getChannel());
            System.out.println("");
        }
