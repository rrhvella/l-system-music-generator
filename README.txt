After checking out and compiling the java code, navigate to the folder containing the compiled files and run the following command in the command line.

java Main C MAJOR A Test

After a few seconds, you should hear a one-to-four-bar phrase in C major, followed by a sustained chord in the same key. The first part itself can be a simple sustained chord, but it can also be a complex melody. 

Once the program is finished playing, a new MIDI file, called 'Test.mid', will also be added to the bin folder. If you play this MIDI file, or analyse it in a notation software (such as Sibelius) you will notice that it is the same exact passage which was played when the program was run.

The name of the generated file can be controlled by modifying the last argument. For example, the following command will store the program output in a file called 'AnotherTest.mid':

java Main C MAJOR A AnotherTest

You can also add spaces to the filename by using quotes. For example, the following command will store the program output in a file called 'Yet another test.mid':

java Main C MAJOR A "Yet Another Test"

The key of the output can be controlled by changing the first two arguments. So, if you wanted to generate a score in A minor, you would enter the following command: 

java Main A MINOR A "Test in A Minor" 

Similarly, if you wanted to generate a score in G# major, the command would be:

java Main GSHARP MAJOR A "Test in G Sharp Major" 

Please note that GSHARP and AFLAT are equivalent, and both are accepted.

The third argument is the structure of the final piece. The system will take this argument, assign a phrase to each unique character and then order and repeat the phrases according to the order and repetition of the original characters. So if the following command were entered:

java Main GSHARP MAJOR ABAB "Test in G Sharp Major 2" 

The system would play the first phrase, then the second, then the first, and finally the second one again, before playing the sustained chord. Similarly, if the following command were entered:

java Main GSHARP MAJOR AAABBBBAC "Test in G Sharp Major 3"

The system  would play the first phrase three times, then the second phrase four times, then the first phrase again, and finally the third one, before playing the sustained chord.

Please note that all of these arguments have to be specified in order to use the system.

 
