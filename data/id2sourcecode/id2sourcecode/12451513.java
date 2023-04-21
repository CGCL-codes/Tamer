    public void testLargeJavaScript() throws Exception {
        boolean quicktest = true;
        boolean showProgress = false;
        int linesToTest[] = { 1000, 10000, 100000, 1000000 };
        int numTests = linesToTest.length;
        int minOptLevel = -2;
        int maxOptLevel = 9;
        int expectMemoryExceededForLinesOver = 100000;
        if (quicktest) {
            numTests = 2;
            minOptLevel = -1;
            maxOptLevel = 1;
            showProgress = false;
        } else {
            showProgress = true;
        }
        for (int optimizationLevel = minOptLevel; optimizationLevel <= maxOptLevel; optimizationLevel++) {
            HttpUnitOptions.setJavaScriptOptimizationLevel(optimizationLevel);
            for (int i = 1; i < numTests; i++) {
                int fromj = 1;
                int toj = linesToTest[i] + 1;
                int lines = toj - fromj;
                String testDesc = "test " + i + " for " + lines + " Lines (" + fromj + "-" + toj + ") at optlevel " + optimizationLevel;
                if (showProgress) System.out.println(testDesc);
                int midj = (fromj + toj) / 2;
                WebConversation wc = null;
                StringBuffer prepareScript = new StringBuffer();
                try {
                    for (int j = fromj; j < toj; j++) {
                        prepareScript.append("var");
                        prepareScript.append(j);
                        prepareScript.append("=");
                        prepareScript.append(j);
                        prepareScript.append("+1;\n");
                    }
                    prepareScript.append("alert(var" + midj + ");");
                    wc = this.doTestJavaScript(prepareScript.toString());
                } catch (RuntimeException re) {
                    if ((optimizationLevel >= 0) && (lines >= 50000)) {
                        this.warnDisabled("testLargeJavaScript", "C", 2, "fails with runtime Exception for " + lines + " lines at optimizationLevel " + optimizationLevel + " the default is level -1 so we only warn");
                    } else {
                        throw re;
                    }
                } catch (java.lang.OutOfMemoryError ome) {
                    if (lines >= expectMemoryExceededForLinesOver) {
                        this.warnDisabled("testLargeJavaScript", "C", 2, "fails with out of memory error for " + lines + " lines at optimizationLevel " + optimizationLevel + " we expect this for more than " + expectMemoryExceededForLinesOver + " lines");
                        break;
                    } else {
                        throw ome;
                    }
                } catch (java.lang.ClassFormatError cfe) {
                    if (optimizationLevel >= 0) this.warnDisabled("testLargeJavaScript", "C", 2, "fails with class format error for " + lines + " lines at optimizationLevel " + optimizationLevel + " the default is level -1 so we only warn"); else throw cfe;
                }
                if (wc != null) {
                    String expected = "" + (midj + 1);
                    expected = expected.trim();
                    assertEquals(testDesc, expected, wc.popNextAlert());
                }
            }
            HttpUnitOptions.reset();
        }
    }
