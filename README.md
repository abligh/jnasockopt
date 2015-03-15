# jnasockopt

This Java module provides JNA support for setsockopt with a limited
degree of platform interoperability.

Usage:

    Socket sock;
    ...
    JNASockOpt.setSockOpt(sock, JNASockOptionLevel.SOL_SOCKET, JNASockOption.SO_RCVBUF, 6553600);
    ...
    JNASockOpt.setSockOpt(sock, JNASockOptionLevel.SOL_TCP, JNASockOption.TCP_KEEPIDLE, 60);

An IOException will be thrown if the socket option is not support or if
`jnasockopt` does not know how to support it.

See JNASockOptTest.java for examples.

## Download

    $ git clone git://github.com/abligh/jnasockopt.git

## Building and Installation

To be completed, but it should be obvious.

## Adding support for another platform

Support for another platform can be generated relatively easily using the perl program
`discover.pl`.

Steps:

1. Ensure you have a C compiler installed (`gcc` assumed currently) and headers
2. Run `discover.pl` and check that produces some output - discard it for now
3. Edit `discover.pl` and add any additional includes necessary for your platform
4. Add any missing socket options or socket option levels to `JNASockOption.java` and `JNASockOptionLevel.java`. Keep these files in alphabetical order
5. Run `discover.pl` and check that produces some output redirecting the output to a file
6. Rename the classes in your generated file appropriately

## To do

Any platform other than Linux / Mac. Contributions welcome.

## Authors

* The original code was written by Alex Bligh.

## License

Please see the file called LICENSE.
