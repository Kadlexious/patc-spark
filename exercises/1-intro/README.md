# Hands-on exercise: prominence calculator

Given the [hamlet.txt](hamlet.txt) file, which follows the next structure:

    # ACT I
    
    ## SCENE I. Elsinore. A platform before the castle.
    
    FRANCISCO at his post. Enter to him BERNARDO
    BERNARDO
    Who's there?
    FRANCISCO
    Nay, answer me: stand, and unfold yourself.
    BERNARDO
    Long live the king!
    FRANCISCO
    Bernardo?
    BERNARDO
    He.

This exercise consists on providing the 5 characters with the higher and lower prominence (that is,
the number of time their names appear in the text). Example of execution:

    *** The 5 names that appear the most are:
    	HAMLET: 359 times.
    	HORATIO: 112 times.
    	KING CLAUDIUS: 102 times.
    	LORD POLONIUS: 86 times.
    	QUEEN GERTRUDE: 69 times.
    *** The 5 names that appear the less are:
    	LUCIANUS: 1 times.
    	VOLTIMAND: 2 times.
    	CORNELIUS: 2 times.
    	PRINCE FORTINBRAS: 6 times.
    	FRANCISCO: 8 times.
  	
The structure of the document is the next:

* An act is marked with a sharp (#) character, followed by ACT N, where N is the roman number for the act
* An scene is marked with a double sharp (##), followed by SCENE N, where N is the roman number for the scene. Followed by the title of the scene
* Each intervention of a character consists on a line with the name of each character that is pronouncing a sentence, in UPPER CASE, followed by the sentence in lowercase.
