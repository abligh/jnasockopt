package org.jnasockopt;

// (c) 2015 Alex Bligh
// Released under the Apache licence - see LICENSE for details

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JNASockOptionDetails {
	static JNASockOptionDetails instance = null;
	
    private Map<JNASockOption, Integer> optionMap;
    private Map<JNASockOptionLevel, Integer> levelMap;
 
	static {
	    String osName = System.getProperty("os.name");
	    if (osName.startsWith("Linux")) {
	        instance = new JNASockOptionDetailsLinux();
	    }
	    else if (osName.startsWith("AIX")) {
	        // TODO: insert AIX bindings
	    }
	    else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
	        instance = new JNASockOptionDetailsMac();
	    }
	    else if (osName.startsWith("Windows CE")) {
	        // TODO: Insert Windows CE bindings
	    }
	    else if (osName.startsWith("Windows")) {
	        // TODO: Insert Windows bindings
	    }
	    else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
	        // TODO: Insert Solaris / SunOS bindings
	    }
	    else if (osName.startsWith("FreeBSD")) {
	        // TODO: Insert FreeBSD bindings
	    }
	    else if (osName.startsWith("OpenBSD")) {
	        // TODO: Insert OpenBSD bindings
	    }
	    else if (osName.equalsIgnoreCase("gnu")) {
	        // TODO: Insert GNU bindings
	    }
	    else if (osName.equalsIgnoreCase("gnu/kfreebsd")) {
	        // TODO: Insert GNU/kfreebsd bindings
	    }
	    else if (osName.equalsIgnoreCase("netbsd")) {
	        // TODO: Insert NetBSD bindings
	    }
	    
	    if (instance == null) {
	    	// empty map
	    	instance = new JNASockOptionDetails();
	    }
	}
	
	protected JNASockOptionDetails () {
		optionMap = new HashMap<JNASockOption, Integer>();
		levelMap = new HashMap<JNASockOptionLevel, Integer>();
	}
	
	protected void putOption(JNASockOption key, int value) {
		optionMap.put(key, value);
	}
	
	public static JNASockOptionDetails getInstance() {
		return instance;
	}
	protected void putLevel(JNASockOptionLevel key, int value) {
		levelMap.put(key, value);
	}

	public int getOption(JNASockOption key) throws IOException {
		Integer option = optionMap.get(key);
		if (option == null) {
			throw new IOException("Bad socket option "+key.toString());
		}
		return option;
	}
	
	public int getLevel(JNASockOptionLevel key) throws IOException {
		Integer level = levelMap.get(key);
		if (level == null) {
			throw new IOException("Bad socket option level "+key.toString());
		}
		return level;
	}
}
