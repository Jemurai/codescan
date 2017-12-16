# codescan

Codescan is a simple tool intended to identify potential security
flaws in code.  It is useful for ensuring that certain patterns
don't exist.  It should not be used as an indication that code is
truly safe as there are things that are difficult to check for and
much more sophisticated ways of analyzing code.

## Installation

The best way to get CodeScan is from github: https://github.com/Jemurai/codescan.

## Usage

    $ java -jar codescan-0.1.0-standalone.jar [args]

    Or from the root of the codescan source:
    $ lein run /target/directory/root/of/application/

## Options

The only real option codescan offers is a simple usage message and
the directory to be analyzed.

## License

Copyright © 2014-2017 Jemurai, LLC.
