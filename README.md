# ProgressBar
Console Progress Bar for Downloads or Lengthy Processes


## Examples:

### With Autoprint

        ConsoleProgressBar bar = new ConsoleProgressBar("Test Progress Bar", 5000000, "MB");

        long progress = 0;
        
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            
            progress += 5000;
            bar.update(progress);
        } while (!bar.isComplete());
        bar.complete();
####

        Test Progress Bar: 
          0% [>                               ]    5000MB/5000000MB - ETA: 00:00:00
        ...
          4% [=>                              ]  245000MB/5000000MB - ETA: 00:00:59
        ...
         54% [=================>              ] 2745000MB/5000000MB - ETA: 00:00:28
        ...
         94% [==============================> ] 4705000MB/5000000MB - ETA: 00:00:03
        ...
        100% [================================] 5000000MB/5000000MB - Complete (1m 3s)

### Without Autoprint

        ConsoleProgressBar bar = new ConsoleProgressBar("Test Progress Bar", 5000000, "MB");
        bar.setAutoPrint(false);

        long progress = 0;
        
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            
            progress += 5000;
            if (bar.update(progress)) {
                bar.print();
            }
        } while (!bar.isComplete());
        bar.complete(true, "All Done");
####

        Test Progress Bar: 
          0% [>                               ]    5000MB/5000000MB - ETA: 00:00:00
        ...
          4% [=>                              ]  245000MB/5000000MB - ETA: 00:00:59
        ...
         54% [=================>              ] 2745000MB/5000000MB - ETA: 00:00:28
        ...
         94% [==============================> ] 4705000MB/5000000MB - ETA: 00:00:03
        ...
        100% [================================] 10000000MB/10000000MB - Complete (1m 3s) All Done


## Usage:

When you create a new Progress Bar, you will pass in the title, the total quantity, and the units of the bar. You can see that these are included in the output or the examples.

By default the Progress Bar will print automatically. To manually decide when to print it turn autoPrint off as in the seconds example.

If you choose to print it manually, the boolean returned from the update() method will indicate whether an update to the progress bar has occurred.

The print() method used System.out.print() not System.out.println() and uses escape characters to clear and rewrite the line keeping the progress bar on one line.

You can call update() as often as you would like with your updated progress value. The progress bar will only reprint itself with a maximum frequency of once every 200 milliseconds, or 5 times per second.

The Progress Bar will automatically calculate an ETA based off of the progress so far and the speed at which it is progressing.

The Progress Bar is completed when the progress has reached the quantity provided initially, which can be determined by calling isComplete().

After completion, the complete() method should be called. This will use System.out.println() to finish the Progress Bar line, as well as optionally add some additional information.

You can pass in two arguments to complete(), whether or not to print the total time elapsed (on by default), and an additional message to include at the end, if any. In the second example above we include the message: "All Done".

There are overloaded constructors where you can set things such as the width of the progress bar, and setting autoPrint without an additional line. You can use setters to update the total quantity during execution, as well as setting an initial progress and initial duration for resumable Progress Bars. There is also a method addOne() as an alternative to update() for specific use cases.

See the source code or unit tests for a more detailed understanding of the features and function of the Progress Bar.
