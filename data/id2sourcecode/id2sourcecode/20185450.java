    public static String getError(int e) {
        switch(e) {
            case 0:
                return new String("No error");
            case 1:
                return new String("Queue node creation failed");
            case 2:
                return new String("Could not open file");
            case 3:
                return new String("The Image Web Server's licensed file limit has been reached ");
            case 4:
                return new String("The requested file is larger than is permitted by the license on this Image Web Server ");
            case 5:
                return new String("Not enough memory for new file");
            case 6:
                return new String("The Image Web Server's licensed client limit has been reached ");
            case 7:
                return new String("Detected duplicate open from net layer");
            case 8:
                return new String("Packet request type not yet implemented");
            case 9:
                return new String("Packet type is illegal");
            case 10:
                return new String("Client closed while requests outstanding");
            case 11:
                return new String("Client UID unknown");
            case 12:
                return new String("Could not create new client ");
            case 13:
                return new String("Could not resolve address of Image Web Server ");
            case 14:
                return new String("Could not connect to host ");
            case 15:
                return new String("Receive timeout");
            case 16:
                return new String("Error sending header ");
            case 17:
                return new String("Error receiving header ");
            case 18:
                return new String("Error sending packet");
            case 19:
                return new String("Error receiving packet");
            case 20:
                return new String("401 Unauthorised");
            case 21:
                return new String("403 Forbidden");
            case 22:
                return new String("Is the host an Image Web Server?");
            case 23:
                return new String("Your HTTP proxy requires authentication,\nthis is presently unsupported by the Image Web Server control");
            case 24:
                return new String("Unexpected HTTP response ");
            case 25:
                return new String("Bad HTTP response ");
            case 26:
                return new String("Already connected");
            case 27:
                return new String("The connection is invalid");
            case 28:
                return new String("Windows sockets failure ");
            case 29:
                return new String("Symbology error");
            case 30:
                return new String("Could not open database");
            case 31:
                return new String("Could not execute the requested query on database");
            case 32:
                return new String("SQL statement could not be executed");
            case 33:
                return new String("Open symbol layer failed");
            case 34:
                return new String("The database is not open");
            case 35:
                return new String("This type of quad tree is not supported");
            case 36:
                return new String("Invalid local user key name specified ");
            case 37:
                return new String("Invalid local machine key name specified ");
            case 38:
                return new String("Failed to open registry key ");
            case 39:
                return new String("Registry query failed ");
            case 40:
                return new String("Type mismatch in registry variable");
            case 41:
                return new String("Invalid arguments passed to function ");
            case 42:
                return new String("ECW error ");
            case 43:
                return new String("Server error ");
            case 44:
                return new String("Unknown error ");
            case 45:
                return new String("Extent conversion failed");
            case 46:
                return new String("Could not allocate enough memory ");
            case 47:
                return new String("An invalid parameter was used ");
            case 48:
                return new String("Could not perform Read/Write on file ");
            case 49:
                return new String("Could not open compression task ");
            case 50:
                return new String("Could not perform compression ");
            case 51:
                return new String("Trying to generate too many output lines");
            case 52:
                return new String("User cancelled compression");
            case 53:
                return new String("Could not read line from input image file");
            case 54:
                return new String("Input image size exceeded for this version");
            case 55:
                return new String("Specified image region is outside image area");
            case 56:
                return new String("Supersampling not supported");
            case 57:
                return new String("Specified image region has a zero width or height");
            case 58:
                return new String("More bands specified than exist in this file ");
            case 59:
                return new String("An invalid band number has been specified ");
            case 60:
                return new String("Input image size is too small to compress");
            case 61:
                return new String("The ECWP client version is incompatible with this server ");
            case 62:
                return new String("Windows Internet Client error ");
            case 63:
                return new String("Could not load wininet.dll ");
            case 64:
                return new String("Invalid SetView parameters or SetView not called.");
            case 65:
                return new String("There is no open ECW file.");
            case 66:
                return new String("Class does not implement ECWProgressiveDisplay interface.");
            case 67:
                return new String("Incompatible coordinate systems");
            case 68:
                return new String("Incompatible coordinate datum types");
            case 69:
                return new String("Incompatible coordinate projection types");
            case 70:
                return new String("Incompatible coordinate units types");
            case 71:
                return new String("Non-linear coordinate systems not supported");
            case 72:
                return new String("GDT Error  ");
            case 73:
                return new String("Zero length packet : ");
            case 74:
                return new String("Must use Japanese version of the ECW SDK");
            case 75:
                return new String("Lost of connection to server  ");
            case 76:
                return new String("NCSGdt coordinate conversion failed  ");
            case 77:
                return new String("Failed to open metabase  ");
            case 78:
                return new String("Failed to get value from metabase  ");
            case 79:
                return new String("Timeout sending header  ");
            case 80:
                return new String("Java JNI error  ");
            case 81:
                return new String("No data source passed");
            case 82:
                return new String("Could not resolve address of Image Web Server Symbol Server Extension");
            case 83:
                return new String("Invalid NCSError value!");
            case 84:
                return new String("End Of File reached  ");
            case 85:
                return new String("File not found ");
            case 86:
                return new String("File is invalid or corrupt  ");
            case 87:
                return new String("Attempted to read, write or seek past file limits  ");
            case 88:
                return new String("Permissions not available to access file  ");
            case 89:
                return new String("File open error ");
            case 90:
                return new String("File close error  ");
            case 91:
                return new String("File IO error  ");
            case 92:
                return new String("Illegal World Coordinates  ");
            case 93:
                return new String("Image projection doesn't match controlling layer  ");
            case 94:
                return new String("Unknown map projection ");
            case 95:
                return new String("Unknown datum ");
            case 96:
                return new String("User specified Geographic Projection Database data server failed while loading .  Please check your network connection and if the problem persists contact the website administrator.");
            case 97:
                return new String("Remote Geographic Projection Database file downloading has been disable and no Geographic Projection Database data is locally available");
            case 98:
                return new String("Invalid transform mode ");
            case 99:
                return new String("coordinate to be transformed is out of bounds ");
            case 100:
                return new String("Layer already exists with this name  ");
            case 101:
                return new String("Layer does not contain this parameter  ");
            case 102:
                return new String("Failed to create pipe ");
            case 103:
                return new String("Directory already exists  ");
            case 104:
                return new String("The path was not found  ");
            case 105:
                return new String("The read was cancelled");
            case 106:
                return new String("Error reading georeferencing data from JPEG 2000 file ");
            case 107:
                return new String("Error writing georeferencing data to JPEG 2000 file");
            case 108:
                return new String("JPEG 2000 file is not or should not be georeferenced");
            case 109:
                return new String("Max NCSError enum value!");
        }
        return new String("");
    }
