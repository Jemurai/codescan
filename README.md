# codescan

Codescan is a simple tool intended to identify potential security
flaws in code.  It is useful for ensuring that certain patterns
don't exist.  It should not be used as an indication that code is
truly safe as there are things that are difficult to check for and
much more sophisticated ways of analyzing code.

Codescan is a code review assist tool, NOT a static analyzer.

## Installation

The best way to get CodeScan is from github: https://github.com/Jemurai/codescan.

Once you have the code, you can just use lein to build it.
`lein jar`

## Usage

If you want to run it from the jar file, it might look like this: 

`java -jar codescan-0.1.0-standalone.jar [args]`

Where the argument is the root of the directory you want it to scan.

You can also run it right from source, which is what we usually do:
`lein run /target/directory/root/of/application/`

Once you've run codescan, you have to look at the results to identify
the files that have potential issues and determine if they are real.
Again, this is a "code review assist" tool, not a static analyzer!

## Options

The only real option codescan offers is a simple usage message and
the directory to be analyzed.

## License

Copyright © 2014-2019 Jemurai, LLC.
