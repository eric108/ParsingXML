ParsingXML
==========
Documentation for test-xml-manipulation
Nov. 8th, 2013
Ziyi Jiang
Email: zjiang42@gatech.edu 
Georgia Tech

This package includes (only listed required items here):
MainSaxApp.java (in “src” folder)
DataFilter.java (in “src” folder)
QuickNote.xml (in “src” folder)
FinalQuickNote.xml (in “src” folder)

	
Target

The package is mainly for four scenarios:

-	The AppHostType ‘Kahua.Silverlight’ is deprecated and should be ‘Kahua.Desktop’
-	The label NumberNameLabel, with value ‘Number’ is missing.
-	The attribute StretchedColumnNumber is deprecated, and should now be StretchColumnNumber
-	On Control tags that contain the attributes ‘VerticalAlignment’ and ‘HorizontalAlignment’, VerticalAlignment should appear before 	HorizontalAlignment.

Implementation:

I used SAX package to parse the given xml file and fully implemented all the required scenarios.

