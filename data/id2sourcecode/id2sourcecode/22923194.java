    void screenCapture(final Integer wnd) {
        currentWindow = wnd;
        Rect r = new Rect();
        User32.INSTANCE.GetWindowRect(wnd, r);
        try {
            Thread.sleep(500);
            User32.INSTANCE.SetForegroundWindow(wnd);
            Thread.sleep(250);
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(new Rectangle(r.left, r.top, r.right - r.left + 1, r.bottom - r.top + 1));
            Thread.sleep(250);
            main.image = img;
            zoom.image = img;
            popupZoom.image = img;
            main.revalidate();
            requestFocus();
            toFront();
            repaint();
            popupZoom.repaint();
        } catch (AWTException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            statusLineToLastError();
        }
    }
