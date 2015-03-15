package org.jnasockopt;

// This enum contains all the socket option levels used on any platform
// A perl script translates these into the appropriate values for each platform

public enum JNASockOptionLevel {
	SOL_SOCKET,
	SOL_LOCAL,
	SOL_AAL,
	SOL_ATALK,
	SOL_ATM,
	SOL_AX25,
	SOL_CAN_BASE,
	SOL_CAN_RAW,
	SOL_DECNET,
	SOL_ICMPV6,
	SOL_IP,
	SOL_IPV6,
	SOL_IPX,
	SOL_IRDA,
	SOL_IRLMP,
	SOL_IRTTP,
	SOL_NDRVPROTO,
	SOL_NETLINK,
	SOL_NETROM,
	SOL_PACKET,
	SOL_RAW,
	SOL_ROSE,
	SOL_SCTP,
	SOL_TCP,
	SOL_TIPC,
	SOL_UDP,
	SOL_X25
}
