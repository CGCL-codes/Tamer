package oop.ex5.protocols;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * This is interface for factory which produces all types of messages in Ex5 systems
 * @author Dima
 *
 */
public interface FileSharingMessageFactory {

    /**
	 * Creates FM-to-NS message from input stream
	 * @param i Input stream which holds FM-to-NS message
	 * @return FM-to-NS message stored in input stream
	 * @throws IOException If some I/O failure occurs
	 */
    public FMtoNSmsg getFMtoNSmessage(DataInputStream i) throws IOException;

    /**
	 * Creates BYE message
	 * @param uploadAddress Address of FM's upload thread
	 * @return New FM-to-NS bye message
	 */
    public FMtoNSmsg getFMtoNSmessage(NetworkAddress uploadAddress);

    /**
	 * Creates REMOVE_FILE/ADD_FILE/GET_FILE messages from file name
	 * @param type One of above message types
	 * @param filename Name of file associated with Remove/add/get message
	 * @param uploadAddress Address of FM's upload thread
	 * @return New FM-to-NS message
	 */
    public FMtoNSmsg getFMtoNSmessage(FMtoNSMsgType type, String filename, NetworkAddress uploadAddress);

    /**
	 * Creates ANNOUNCE message from list of file names and network address
	 * @param fileList List of file names available in FM's database
	 * @param uploadAddress Address of FM's upload thread
	 * @return FM-to-NS ANNOUNCE message created from file list and FM network address
	 */
    public FMtoNSmsg getFMtoNSmessage(Collection<String> fileList, NetworkAddress uploadAddress);

    /**
	 * Creates message from input stream
	 * @param i Input stream which holds NS-to-FM message
	 * @throws IOException If some I/O failure occurs
	 */
    public NStoFMmsg getNStoFMmessage(DataInputStream i) throws IOException;

    /**
	 * Creates OK, FILE_NOT_FOUND, INVALID_SESSION, REANNOUNCE messages
	 * @param msgName
	 * @throws IOException If some I/O failure occurs
	 */
    public NStoFMmsg getNStoFMmessage(NStoFMmsgType msgName) throws IOException;

    /**
	 * Creates NS-to-FM message with set of FileManagers
	 * This message generally used as response to GET request for some file 
	 * @param l List of FileManager addresses which hold some file in database
	 * @return New NS-to-FM message with set of FM addresses
	 */
    public NStoFMmsg getNStoFMmessage(Set<NetworkAddress> l);

    /**
	 * Restores file manager download request from stream
	 * @param i Input stream which holds FM-to-FM message
	 * @return Message created from given input stream
	 * @throws IOException If some I/O failure occurs
	 */
    public FMdownloadMsg getFMdownloadMessage(DataInputStream i) throws IOException;

    /**
	 * Creates new download request message based of file name
	 * @param fileName The file id(name) of requested file
	 * @return New FM-to-FM message which requests a file from some FM  
	 */
    public FMdownloadMsg getFMdownloadMessage(String fileName);

    /**
	 * Creates Message from input stream
	 * If there is a file in message, this file
	 * is stored inside file pointed by tempFile parameter
	 * @param tempFile Location to store file included in message 
	 * @param i Input stream which holds FM-to-FM message
	 * @throws IOException If some I/O failure occurs
	 * @return FM-to-FM message which may contain a file (received file stored on disk) 
	 */
    public FMuploadMsg getFMuploadMessage(DataInputStream i, File tempFile) throws IOException;

    /**
	 * Creates FILE_CONTENTS message from file
	 * This message includes contests of specific file
	 * Assumption: file remains unchanged on disk till the end of last
	 * .send() call for this message
	 * @param file The file being send
	 * @return New message that can be used to send file contents
	 */
    public FMuploadMsg getFMuploadMessage(File file);

    /**
	 * Creates FILE_NOT_FOUND and INVALID_SESSION FM-to-FM messages
	 * @param t Type of message
	 * @return New FM-to-FM response message
	 */
    public FMuploadMsg getFMuploadMessage(FMuploadMsgType t);
}
