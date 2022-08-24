# ProgressBar

## Console Progress Bar for Downloads or Lengthy Processes

***

## Examples

### With Autoprint

        ProgressBar bar = new ProgressBar("Test Progress Bar", 50000, "MB");

        long progress = 0;
        
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            
            progress += 100;
            bar.update(progress);
        } while (!bar.progressComplete());
        bar.complete();

####

        Test Progress Bar: 
          0% [>                               ]    100MB/50,000MB at 0.0MB/s - ETA: 00:00:00  
        ...
          4% [=>                              ]  2,100MB/50,000MB at 1,978.6MB/s - ETA: 00:00:23  
        ...
         54% [=================>              ] 27,300MB/50,000MB at 1,978.7MB/s - ETA: 00:00:11  
        ...
         94% [==============================> ] 47,300MB/50,000MB at 1,977.8MB/s - ETA: 00:00:01  
        ...
        100% [================================] 50,000MB/50,000MB - Complete (25s) 

### Without Autoprint

        ProgressBar bar = new ProgressBar("Test Progress Bar", 5000, "MB");
        bar.setAutoPrint(false);

        long progress = 0;
        
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            
            progress += 100;
            if (bar.update(progress)) {
                bar.print();
            }
        } while (!bar.progressComplete());
        bar.complete(true, "All Done");

####

        Test Progress Bar: 
          0% [>                               ]    100MB/50,000MB at 0.0MB/s - ETA: 00:00:00  
        ...
          4% [=>                              ]  2,100MB/50,000MB at 1,978.6MB/s - ETA: 00:00:23  
        ...
         54% [=================>              ] 27,300MB/50,000MB at 1,978.7MB/s - ETA: 00:00:11  
        ...
         94% [==============================> ] 47,300MB/50,000MB at 1,977.8MB/s - ETA: 00:00:01  
        ...
        100% [================================] 50,000MB/50,000MB - Complete (25s) - All Done

 
***

## Usage

When you create a new Progress Bar, you will pass in the title, the total quantity, and the units of the bar. You can see that these are included in the output or the examples.

By default, the Progress Bar will print automatically. To manually decide when to print it instead, turn autoPrint off as in the second example.

If you choose to print it manually, the boolean returned from the update() method will indicate whether an update to the Progress Bar has occurred.

The print() method uses System.out.print() not System.out.println() because it uses escape characters to clear and rewrite the line keeping the Progress Bar on one line.

You can call update() as often as you would like with your updated progress value. The Progress Bar will only update and reprint itself with a maximum frequency of once every 200 milliseconds, or 5 times per second.

You can also call addOne() as an alternative to update() for specific use cases.

The Progress Bar will automatically calculate an ETA based off of the progress so far and the speed at which it is progressing.

The Progress Bar is complete when the progress has reached the quantity provided initially, which can be determined by calling progressComplete().

After completion, the complete() method should be called. This will use System.out.println() to finalize the Progress Bar line, as well as optionally add some additional information.

The complete() method could also be called at any time to complete the Progress Bar. You can pass in two arguments to complete(), whether to print the total time elapsed (on by default), and an additional message to include at the end, if any. In the second example above we include the message: "All Done".

The fail() method can be called at any time to fail the Progress Bar. This will use System.out.println() to finalize the Progress Bar line, as well as optionally add some additional information.

You can pass in two arguments to fail(), whether to print the total time elapsed (on by default), and an additional message or error message to include at the end, if any.

There are overloaded constructors where you can set things such as the width of the Progress Bar, and turning autoPrint on or off without an additional line.

You can also use the setters to modify aspects of the Progress Bar:
* Update the title if the Progress Bar has not been printed yet
* Update the total quantity and unit type during execution
* Set an initial progress and initial duration for resumable Progress Bars
* Turn auto print on or off
* Set the colors of the Progress Bar
* Set an indent before the Progress Bar
* Turn comma separation on or off
* Show or hide certain elements of the Progress Bar

If you subclass ProgressBar, you can overload the processLog() methods to conditionally update the Progress Bar based on logs that are passed to the method. You can also mark the log as an error log by setting the second parameter to true; these can be stored or processed differently, or used to complete or fail the Progress Bar based on your own use case. 

See the source code or unit tests for a more detailed understanding of the features and function of the Progress Bar.

 
***
