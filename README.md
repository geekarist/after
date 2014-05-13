After
=====

Cross-platform countdown timer application with tray icon, written in Java.
Inspired by [Orzeszek Timer](http://www.orzeszek.org/dev/timer/).

Usage
-----

    after <seconds> [command]

Features
--------

- After some time, show an alarm popup
- The tray icon shows the remaining time
- Choose time when launching
- Packaged as an executable jar

Planned
-------

- Create shell script for executing
- Integration & unit tests (use UISpec4J?)
- Improve application appearance
- Launch a custom shell command when time elapsed

Develop
-------

- Install textimagegenerator in local maven repository:

        git clone https://github.com/jcraane/textimagegenerator
        cd textimagegenerator
        mvn install
