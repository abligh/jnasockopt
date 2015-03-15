package org.jnasockopt;

// (c) 2015 Alex Bligh
// Released under the Apache licence - see LICENSE for details

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class JNASockOpt {

    private static Field fdField;
   
    static {
    	Native.register("c");
        try {
            fdField = FileDescriptor.class.getDeclaredField("fd");
            fdField.setAccessible(true);
        } catch (Exception ex) {
            fdField = null;
        }
    }
    
    public static int getInputFd(Socket s) {
        try {
            FileInputStream in = (FileInputStream)s.getInputStream();
            FileDescriptor fd = in.getFD();
            return fdField.getInt(fd);
        } catch (Exception e) { } 
        return -1; 
    }   
 
    public static int getOutputFd(Socket s) {
        try {
            FileOutputStream in = (FileOutputStream)s.getOutputStream();
            FileDescriptor fd = in.getFD();
            return fdField.getInt(fd);
        } catch (Exception e) { } 
        return -1; 
    }   
 
    public static int getFd(Socket s) {
    	int fd = getInputFd(s);
    	if (fd != -1)
    		return fd;
    	return getOutputFd(s);
    }

    // The list of SOL_ and SO_ options is platform dependent
	public static final int SOL_SOCKET = 0xffff;
    public static final int SO_RCVBUF = 0x1002; // that's under OS-X, but it's 8 under Linux
	
    private static native int setsockopt(int fd, int level, int option_name, Pointer option_value, int option_len) throws LastErrorException;

    public static void setSockOpt (Socket socket, JNASockOptionLevel level, JNASockOption option, int option_value) throws IOException {
    	if (socket == null)
    		throw new IOException("Null socket");
    	int fd = getFd(socket);
    	if (fd == -1)
    		throw new IOException("Bad socket FD");
    	IntByReference val = new IntByReference(option_value);
    	int lev = JNASockOptionDetails.getInstance().getLevel(level);
    	int opt = JNASockOptionDetails.getInstance().getOption(option);
    	try {
    	    setsockopt(fd, lev, opt, val.getPointer(), 4);
    	} catch (LastErrorException ex) {
    	    throw new IOException("setsockopt: " + strerror(ex.getErrorCode()));
    	}
    }
    
    public static native String strerror(int errnum);
    
	private JNASockOpt() {
	}
}
