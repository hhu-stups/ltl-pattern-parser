LTL parser for ProB
=========

This parser was part of Philipp Kantner's [Masterthesis](http://stups.hhu.de/w/Muster_f%C3%BCr_die_Verifikation_von_Formeln_in_Temporaler_Logik). It allows to express typical patterns of LTL formula in a simpler language. The pattern are taken from http://patterns.projects.cis.ksu.edu/. 


## Building
The [gradle build tool](http://www.gradle.org) is required to build the parser.

```gradle jar``` will build the jar file.

## Import into Eclipse
If you want to import the parser into Eclipse, you have run ```gradle generateGrammarSource eclipse```.
Because of a [bug](https://issues.gradle.org/browse/GRADLE-3323) in the current ANTLR plugin for gradle, the generated files are not in the correct folder.
