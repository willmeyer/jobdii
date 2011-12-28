# jOBDII

A Java library for communicating with devices over the [OBDII protocol](http://en.wikipedia.org/wiki/On-board_diagnostics#OBD-II).

The library allows you to build applications that communicate with an OBDII device.  It provides a generic interface as
well as a specific implementation for devices that use the [ELM-compatible chipset](http://elmelectronics.com/obdic.html) over a (real or virtual) RS-232
serial port.

Put differently, you can use this to talk to the computer in your car.

### Applications

Your car knows a lot of things, OBDII is the simplest way to access it.

See my [car hacking](http://www.willmeyer.com/things/car-hacking/electronics) stuff for more information on how I
personally use this.

### Requirements & Setup

To be able to use the library, you'll need:

- The [jrs232 package](http://www.github.com/willmeyer/jrs232) package, which exposes `com.willmeyer.jrs232', and its
  own installation requirements
- An ELM-compatible device connected via a real or virtual serial port (search for ELM 327 serial interface devices).


### Using the Library

See `com.willmeyer.jobdii.Elm32x`.

You can learn more about the protocol itself in the [included datasheet](https://github.com/willmeyer/jobdii/blob/master/docs/OBDPro_Datasheet.pdf).
