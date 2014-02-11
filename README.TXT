WARNING: THIS CODE WAS ALTERED FOR CLASSROOM USE. IT DOES NOT CURRENTLY
HAVE A CLASS TO LOAD THE DTD FROM AN INPUT SOURCE. THE REST FUNCTIONS OK.
RUN THE TESTS!

-------------------------------------
DTD Reader dtdreader.sourceforge.net $Revision: 1.3 $ 
by Helder da Rocha (hlsr@uol.com.br)
Version 0.2 Alpha

This DTD parser was extracted from a larger project, which did simple XML generation. 
Although a few other good DTD parsers are now avaliable on the Net, I decided to
continue this project since its interface is used by several other projects.

I expected with this release to support full DTD parsing, but this version still lacks
support for:
- nested dtd subsets (via external parameter entities)
- conditional inclusion (<![INCLUDE[...]]> and <![IGNORE[...]]> tags)

The classes that support the above features exist but no test cases were implemented
to test them.

Sao Paulo, Brazil
September 19, 2001

-------------------------
Changes: January 25, 2002
- Code review
- Use this package in XURG.
