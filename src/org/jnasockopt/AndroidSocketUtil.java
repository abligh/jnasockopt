import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;

public class AndroidSocketUtil {

	/*
	 * you can get the following constants from JNASockOptionDetailsLinux (for
	 * android system)
	 */
	private final static int SOL_TCP = 6;
	private final static int TCP_KEEPIDLE = 4;
	private final static int TCP_KEEPINTVL = 5;
	private final static int TCP_KEEPCNT = 6;

	/**
	 * The method uses java reflection to set socket options in Android system
	 * and it's only available for Android 4.0 or later versions
	 * 
	 * @param socket
	 *            the Socket object which you want to change its socket options
	 * @param idleTimeout
	 *            timeout value
	 * @param interval
	 *            interval time
	 * @param count
	 *            the time of reconnection
	 */
	protected static void setKeepaliveSocketOptions(Socket socket,
			int idleTimeout, int interval, int count) {
		try {
			socket.setKeepAlive(true);
			try {
				Field socketImplField = Class.forName("java.net.Socket")
						.getDeclaredField("impl");
				socketImplField.setAccessible(true);
				if (socketImplField != null) {
					Object plainSocketImpl = socketImplField.get(socket);
					Field fileDescriptorField = Class.forName(
							"java.net.SocketImpl").getDeclaredField("fd");
					if (fileDescriptorField != null) {
						fileDescriptorField.setAccessible(true);
						FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorField
								.get(plainSocketImpl);
						Class libCoreClass = Class
								.forName("libcore.io.Libcore");
						Field osField = libCoreClass.getDeclaredField("os");
						osField.setAccessible(true);
						Object libcoreOs = osField.get(libCoreClass);
						Method setSocketOptsMethod = Class.forName(
								"libcore.io.ForwardingOs").getDeclaredMethod(
								"setsockoptInt", FileDescriptor.class,
								int.class, int.class, int.class);
						if (setSocketOptsMethod != null) {
							setSocketOptsMethod.invoke(libcoreOs,
									fileDescriptor, SOL_TCP, TCP_KEEPIDLE,
									idleTimeout);
							setSocketOptsMethod.invoke(libcoreOs,
									fileDescriptor, SOL_TCP, TCP_KEEPINTVL,
									interval);
							setSocketOptsMethod
									.invoke(libcoreOs, fileDescriptor, SOL_TCP,
											TCP_KEEPCNT, count);
						}
					}
				}
			} catch (Exception reflectionException) {
			}
		} catch (SocketException e) {
		}
	}
}
