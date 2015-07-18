9 Degrees of Freedom Block for Java
===================================


Java library for Intel Edison Sparkfun 9DOF block

Directly inspired by
[9dofBlock](https://github.com/smoyerman/9dofBlock)

Work In Progress
----------------

I started out intending to use the [SWIG](http://swig.org/) Java binding for [libmraa](https://github.com/intel-iot-devkit/mraa) (libmraajava) but since that library isn't installed on the Edison by default (and required some effort to get cross compiled) I'm now considering an old favorite: [JNA](https://github.com/twall/jna).  Which raises another interesting question about a proper way to build Java on/for Edison since my usual favorite build tools, maven and gradle, are unavailable via opkg.

