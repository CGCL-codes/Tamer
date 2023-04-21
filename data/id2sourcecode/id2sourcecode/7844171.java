    private void browseFile(String theIPAdress, String theFileName, int[] theSocketParam, HttpServletResponse theResponse, UserContext theUserContext) throws GenericGuiException, InvalidSessionException {
        trace(9, "[browseFile] Begin", theUserContext);
        if (theSocketParam == null || theSocketParam[0] == 0) {
            trace(5, "[browseFile] SocketParam null or empty.", theUserContext);
            browseFileError(theFileName, theResponse, theUserContext);
            return;
        }
        int myPortSocket = theSocketParam[1];
        java.net.Socket mySocket = null;
        java.io.InputStream myInput = null;
        ServletOutputStream mySOS = null;
        try {
            int myTotalFileByte = theSocketParam[0];
            trace(7, "[browseFile] Open client socket", theUserContext);
            mySocket = new java.net.Socket(theIPAdress, myPortSocket);
            myInput = mySocket.getInputStream();
            trace(7, "[browseFile] InputStream retrieved", theUserContext);
            int myBufferSize = 4096;
            byte[] myRequestBody = new byte[myBufferSize];
            byte[] myEndBody = null;
            int myNbByteRead = 0, myTotalByteRead = 0, myTotalByteWritten = 0;
            String myContentType = "text/plain";
            theResponse.setContentType(myContentType);
            trace(7, "[browseFile] Response Content-Type set (" + myContentType + ").", theUserContext);
            theResponse.setContentLength(myTotalFileByte);
            trace(7, "[browseFile] Response Content-Length set (" + myTotalFileByte + ").", theUserContext);
            mySOS = theResponse.getOutputStream();
            while ((myNbByteRead = myInput.read(myRequestBody)) != -1) {
                myTotalByteRead += myNbByteRead;
                if (myTotalByteRead > myTotalFileByte) {
                    trace(7, "[browseFile] Try to write more bytes than content-length: " + myTotalByteRead + " bytes read for " + myTotalFileByte + " bytes declared (current buffer: " + myNbByteRead + " bytes)", theUserContext);
                    myNbByteRead = myTotalFileByte - (myTotalByteRead - myNbByteRead);
                    trace(7, "[browseFile] -> Cut the extra part and write only " + myNbByteRead + " bytes", theUserContext);
                }
                if (myNbByteRead != myBufferSize) {
                    trace(7, "[browseFile] Try to write less than buffer size (" + myBufferSize + "): " + myNbByteRead + " bytes", theUserContext);
                    myEndBody = new byte[myNbByteRead];
                    System.arraycopy(myRequestBody, 0, myEndBody, 0, myNbByteRead);
                    mySOS.write(myEndBody);
                } else {
                    mySOS.write(myRequestBody);
                }
                myTotalByteWritten += myNbByteRead;
                trace(9, "[browseFile] Write " + myNbByteRead + " bytes of Request Body in ServletOutputStream (" + myTotalByteWritten + ")", theUserContext);
            }
            trace(7, "[browseFile] " + myTotalByteWritten + " bytes written in ServletOutputStream.", theUserContext);
        } catch (IOException ioe) {
            trace(2, "[browseFile] IOException: " + ioe.getMessage(), theUserContext);
            ioe.printStackTrace();
            browseFileError(theFileName, theResponse, theUserContext);
            throw new GenericGuiException(GenericGuiException.ERR_BROWSE, ioe.getMessage());
        } finally {
            try {
                if (myInput != null) {
                    myInput.close();
                }
                if (mySocket != null) {
                    mySocket.close();
                }
                if (mySOS != null) {
                    mySOS.flush();
                    mySOS.close();
                }
            } catch (Exception e) {
                mySocket = null;
                trace(2, "[browseFile] Exception during socket & stream closure: " + e.getMessage(), theUserContext);
            }
        }
        trace(9, "[browseFile] End", theUserContext);
    }
