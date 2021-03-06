After
=====

Cross-platform countdown timer application with tray icon, written in Java.
Inspired by [Orzeszek Timer](http://www.orzeszek.org/dev/timer/).

Usage
-----

    after <duration>

Features
--------

- After some time, show an alarm popup
- The tray icon shows the remaining time
- Choose time when launching
- Packaged as an executable jar
- Shell script for executing with command line

Planned
-------

- Parse duration expressed as "XX min"
- Print custom message when time elapsed
- Launch a custom shell command when time elapsed
- Improve application appearance

Develop
-------

- Install textimagegenerator in local maven repository:

        git clone https://github.com/jcraane/textimagegenerator
        cd textimagegenerator
        mvn install
