# BookletBuilder

BookletBuilder is a [Java](https://java.com) Package for building booklets. It
can be run as a standalone application or as a library in other tools.

## Supported Conversions

At the moment, the tool only supports a duplex, 4-up imposition where the
pages are cut, stacked and folded:

![4-step process](https://raw.githubusercontent.com/erikogan/BookletBuilder/main/src/main/resources/instructions/steps.png)

## Usage

BookletBuilder is a Java application, so you need to
[have Java installed](https://www.java.com/en/download/help/download_options.xml)
before using it.

The distribution JAR file should include all of its prerequisites and not
require any further libraries or downloads.

### Standalone Application

#### Graphical User Interface

The application provides a _very_ simple graphical interface. Double-clicking
on the JAR file should bring up an open-file dialog box to select your PDF
file for conversion. This will be followed by a Save dialog to save the
resulting booklet file.

#### Command-Line

On the command-line, you can pass an optional argument that will disable
inclusion of printing instructions on the first two pages:

```
% java -jar [path/to/jar/]BookletBuilder-all.jar [--skipInstructions] <in_file> <out_file>
```

If the <in_file> and/or the <out_file> are not provided, the GUI interface
will be used to prompt for the missing information.

#### Docker

If you prefer, the application is available as a Docker image, and can be invoked easily:

```
% docker run -v $PWD:/data erikogan/bookletbuilder /data/in.pdf /data/out.pdf
```

It is recommended that you supply both arguments to the container. It is
likely possible to forward X11 connections out of the container to use the
GUI, but that usage is left as an exercise to the reader.

### In Java Code

The library tries to abstract as much away for users as possible. The minimum
code required to create a booklet from another PDF would be:

```java
package com.stealthymonkeys.pdf.example;

import java.io.IOException;

import com.stealthymonkeys.pdf.FourUpBookletStrategy;

public class BookletBuilderExample {
  // Usage: java com.stealthymonkeys.pdf.example.BookletBuilderExample <in_file> <out_file>
  public static void main(String[] args) throws IOException {
    FourUpBookletStrategy strategy = new FourUpBookletStrategy(args[0], args[1]);
    strategy.impose();
  }
}
```

You might also check out [BookletBuilder.java](https://github.com/erikogan/BookletBuilder/blob/main/src/main/java/com/stealthymonkeys/pdf/BookletBuilder.java)
for a slightly more complex example.

Once there are multiple imposition Strategies we will also include an Abstract
Factory to facilitate users selecting the right Strategy for their needs.

## License

Copyright © 2018 Stealthy Monkeys Consulting, some rights reserved.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU Affero General Public License version 3 as published by
the Free Software Foundation: https://www.gnu.org/licenses/agpl-3.0.en.html

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
details.
