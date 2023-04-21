    private static URL lookForDefaultThemeFile(String aFilename) {
        try {
            XPathFactory fabrique = XPathFactory.newInstance();
            XPath environnement = fabrique.newXPath();
            URL url = new URL("file:" + aFilename);
            InputSource source = new InputSource(url.openStream());
            XPathExpression expression;
            expression = environnement.compile("/alloy/instance/@filename");
            String resultat = expression.evaluate(source);
            AlsActivator.getDefault().logInfo("Solution coming from " + resultat);
            IPath path = new Path(resultat);
            IPath themePath = path.removeFileExtension().addFileExtension("thm");
            File themeFile = themePath.toFile();
            if (themeFile.exists()) {
                AlsActivator.getDefault().logInfo("Found default theme " + themeFile);
                return themeFile.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            AlsActivator.getDefault().log(e);
        } catch (IOException e) {
            AlsActivator.getDefault().log(e);
        } catch (XPathExpressionException e) {
            AlsActivator.getDefault().log(e);
        }
        return null;
    }
