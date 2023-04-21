    public static void createScreenshot(Rectangle rect) {
        Exception ex = null;
        try {
            Class jaiClass = Class.forName("javax.media.jai.JAI");
            Class renderedImageClass = Class.forName("java.awt.image.RenderedImage");
            Method createMethod = jaiClass.getMethod("create", new Class[] { String.class, renderedImageClass, Object.class, Object.class, Object.class });
            Class botClass = Class.forName("java.awt.Robot");
            Constructor botConstructor = botClass.getConstructor(new Class[] {});
            Object bot = botConstructor.newInstance(new Object[] {});
            Method screenCapMethod = botClass.getMethod("createScreenCapture", new Class[] { Rectangle.class });
            Object bimage = screenCapMethod.invoke(bot, new Object[] { rect });
            System.out.println("bimage is " + bimage);
            String outfile = "./capture.png";
            FileOutputStream outstream = new FileOutputStream(outfile);
            createMethod.invoke(null, new Object[] { "encode", bimage, outstream, "PNG", null });
            createMethod.invoke(null, new Object[] { "filestore", bimage, outfile, "PNG", null });
            outstream.close();
            Image image = (Image) bimage;
            AppUtil.screenShotCounter++;
            ScreenShotWindow screenShotWindow = new ScreenShotWindow(image);
            screenShotWindow.setSize(image.getWidth(null) + 100, image.getHeight(null) + 100);
            AppUtil.center(screenShotWindow);
            String title = ResourceUtil.getResource(AppUtil.class, "title.screenshot_window", Application.getApplicationName(), new Integer(AppUtil.screenShotCounter));
            screenShotWindow.setTitle(title);
            screenShotWindow.toFront();
            screenShotWindow.setVisible(true);
        } catch (FileNotFoundException fx) {
            ex = fx;
        } catch (IOException iox) {
            ex = iox;
        } catch (ClassNotFoundException cnfe) {
            ex = cnfe;
        } catch (NoSuchMethodException nsme) {
            ex = nsme;
        } catch (InstantiationException ie) {
            ex = ie;
        } catch (IllegalAccessException iae) {
            ex = iae;
        } catch (InvocationTargetException ite) {
            ex = ite;
        }
        if (ex != null) {
            Log.log(Log.ERROR, new Throwable(), ex, ResourceUtil.getMessage(AppUtil.class, "exception_while_screenshotting"));
        }
    }
