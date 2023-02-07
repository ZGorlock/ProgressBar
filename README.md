
# ProgressBar


## Console Progress Bar for Downloads or Lengthy Processes


----


### Table of Contents:

- [**Examples**](#examples)
  - [With Autoprint](#with-autoprint)
  - [Without Autoprint](#without-autoprint)

+ [**Usage**](#usage)
  + [Instantiation](#instantiation)
  + [Printing](#printing)
  + [Updating](#updating)
  + [Progress](#progress)
  + [Completion](#completion)
  + [Customization](#customization)
  + [Subclassing](#subclassing)


----


# Examples


## With Autoprint

```java
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
```

```
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
```


## Without Autoprint

```java
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
```

```
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
```


&nbsp;

----


# Usage


## Instantiation

- When you create a new Progress Bar, you will pass in the title, the total quantity, and the units of the bar
  - You can see in the [examples](#examples) that these are incorporated into the printed output
- There are overloaded constructors where you can:
  - Set the width of the Progress Bar
  - Enable or disable automatic printing without an additional line


## Printing

- By default, the Progress Bar will print automatically
  - To choose to manually print it instead, disable auto print as shown in the [second example](#without-autoprint)
  - If you choose to print it manually, the boolean returned from the **update()** method will indicate whether an update to the Progress Bar has occurred

+ The **print()** method uses **System.out.print()** not **System.out.println()**
  + This uses escape characters to clear and rewrite the printed output to keep the Progress Bar on the same line
+ The **complete()** and **fail()** methods use **System.out.println()**
  + This finalizes the Progress Bar and moves to the next line
  + Even when auto print is enabled, the Progress Bar will not automatically call **complete()**; this must be called manually _(see_ [Completion](#completion)_)_


## Updating

- You can call **update()** as often as you would like with the current progress value
  - The Progress Bar will only update and reprint itself with a maximum frequency of once every 200 milliseconds, or 5 times per second
- You can also call **addOne()** as an alternative to **update()** for specific use cases


## Progress

- The Progress Bar will calculate an ETA based off of the current progress completed so far and the speed at which it is progressing
- The Progress Bar is complete when the progress has reached the total quantity provided initially
  - You can determine whether the progress of the Progress Bar is complete by calling **progressComplete()**


## Completion

- After completion, the **complete()** method should be called to finalize the Progress Bar
  - The **complete()** method can also be called at any time to immediately complete the Progress Bar
- You can pass two arguments to the **complete()** method:
  - Whether to print the total time elapsed _(enabled by default)_
  - An additional message to print at the end, if any
    - In the [second example](#without-autoprint) we include the message: `"All Done"`
- You can determine whether the Progress Bar is complete and finalized by calling **isCompleted()**

+ The **fail()** method can be called at any time to fail the Progress Bar
  + This is equivalent to calling **complete()** but will show the Progress Bar as having failed
  + You can pass the same two arguments to the **fail()** method as the **complete()** method
+ You can determine whether the Progress Bar is failed by calling **isFailed()**


## Customization

- There are several methods you can call to modify aspects of the Progress Bar:
  - **updateTitle()** will change the title 
    - This change will only succeed if the Progress Bar has not been printed yet
  - **updateTotal()** will change the total quantity
  - **updateUnits()** will change the units
    - This can also optionally scale the current progress if the new units are larger or smaller
  - **setAutoPrint()** will enable or disable automatic printing
  - **setUseCommas()** will enable or disable the use of commas in the printed output
  - **setIndent()** will set the indentation of the printed output
 
+ If a Progress Bar is being resumed from a previous session you can set its initial state:
  + **defineInitialProgress()** will set the previous progress
  + **defineInitialDuration()** will set the previous duration
+ These methods can only be called once, and should be called before any new progress is made

- Individual portions of the printed output can be hidden or shown:
  - **setShowPercentage()** will hide or show the percentage
  - **setShowBar()** will hide or show the bar
  - **setShowRatio()** will hide or show the ratio
  - **setShowSpeed()** will hide or show the speed
  - **setShowTimeRemaining()** will hide or show the time remaining

+ The colors used in the printed output can be customized:
  + **setBaseColor()** will set the base color
  + **setGoodColor()** will set the good color
  + **setBadColor()** will set the bad color
  + **setColors()** allows you to set all three colors at once


## Subclassing

- To further customize the Progress Bar for a specific use case you can extend the **_ProgressBar_** class
- Overload the **processLog()** methods to conditionally update the Progress Bar based on logs that are passed to the method
  - You can choose to call **update()**, **complete()**, or **fail()** depending on the log received
  - Passing `true` as the second argument will indicate that it is an error log
    - Error logs can be stored or processed differently based on your use case 

\
See the source code and unit tests for a more detailed understanding of the function and capabilities of the Progress Bar.


&nbsp;

----
