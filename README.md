[WIP] BakeIt
======

BakeIt is a command line utility to [Pastery](https://www.pastery.net), the best
 pastebin in the world. BakeIt aims to be simple to use and unobtrusive.

Installation
------------

You'll need Java SE 11 or later installed somewhere on your system and on the path,
as well as Clojure 1.10.0 or later.

On macOS, you can do this using Homebrew as follows:

```
brew cask install adoptopenjdk
brew install clojure
```

To install BakeIt,

TODO.

You are done!

Usage
-----

Using BakeIt is similarly easy. First, create a file with your Pastery
API key in `~/.config/bakeit.cfg`, like so:

    [pastery]
    api_key = eisha8ahqui7Aesh0fasyu8HFsdo

Then, just pass the file you want to upload to the `bakeit` command:

    $ bakeit myfile.txt
    Paste URL: https://www.pastery.net/oniasd/

You can also pipe stuff to it:

    $ cat myfile.txt | grep hello | bakeit
    Paste URL: https://www.pastery.net/oxczvs/

The config file
---------------

A full config file can look like this:

    [pastery]
    api_key = eisha8ahqui7Aesh0fasyu8HFsdo

The `api_key` parameter, as explained above, is mandatory. You can find
it at the bottom of [your Pastery account page](https://www.pastery.net/account/).

Happy pasting!

