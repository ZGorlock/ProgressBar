/*
 * File:    ProgressBar.java
 * Package:
 * Author:  Zachary Gill
 */

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import resource.Console;

/**
 * A progress bar for the console.
 */
public class ProgressBar {
    
    //Constants
    
    /**
     * The default width of the progress bar in characters.
     */
    public static final int DEFAULT_PROGRESS_BAR_WIDTH = 32;
    
    /**
     * The default value of the flag indicating whether or not to automatically print the progress bar after an update.
     */
    public static final boolean DEFAULT_PROGRESS_BAR_AUTO_PRINT = true;
    
    /**
     * The minimum number of nanoseconds that must pass before an update can occur.
     */
    public static final long PROGRESS_BAR_MINIMUM_UPDATE_DELAY = TimeUnit.MILLISECONDS.toNanos(200);
    
    
    //Fields
    
    /**
     * The title to display for the progress bar.
     */
    private String title;
    
    /**
     * The total progress of the progress bar.
     */
    private long total;
    
    /**
     * The current progress of the progress bar.
     */
    private long progress = 0;
    
    /**
     * The currently completed progress of the progress bar.
     */
    private long current = 0;
    
    /**
     * The completed size of the progress at the time of the last update.
     */
    private long previous = 0;
    
    /**
     * The initial progress of the progress bar.
     */
    private long initialProgress = 0;
    
    /**
     * The initial duration of the progress bar in seconds.
     */
    private long initialDuration = 0;
    
    /**
     * The time of the current update of the progress bar.
     */
    private long currentUpdate = 0;
    
    /**
     * The time of the previous update of the progress bar.
     */
    private long previousUpdate = 0;
    
    /**
     * The time the progress bar was updated for the firstUpdate time.
     */
    private long firstUpdate = 0;
    
    /**
     * The width of the bar in the progress bar.
     */
    private int width;
    
    /**
     * The units of the progress bar.
     */
    private String units;
    
    /**
     * A flag indicating whether or not to automatically print the progress bar after an update.
     */
    private boolean autoPrint;
    
    /**
     * The current progress bar.
     */
    private String progressBar = "";
    
    /**
     * A flag indicating whether there was an update to the progress bar or not.
     */
    private boolean update = false;
    
    
    //Main Method
    
    /**
     * The main method.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        ProgressBar progressBar = new ProgressBar("Title of Progress Bar", 10000, "ms");
        
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        long time;
        do {
            time = System.currentTimeMillis() - startTime;
            progressBar.update(time);
        } while (time < 10000);
        progressBar.complete();
        System.out.println("Done");
        System.out.println();
        
        progressBar = new ProgressBar("Title of Progress Bar", 10000, "ms");
        progressBar.autoPrint = false;
        System.out.println("Start (No AutoPrint)");
        startTime = System.currentTimeMillis();
        do {
            time = System.currentTimeMillis() - startTime;
            if (progressBar.update(time)) {
                progressBar.print();
            }
        } while (time < 10000);
        progressBar.complete();
        System.out.println("Done");
    }
    
    
    //Constructors
    
    /**
     * Creates a new ConsoleProgressBar object.
     *
     * @param title     The title to display for the progress bar.
     * @param total     The total size of the progress bar.
     * @param width     The with of the bar in the progress bar.
     * @param units     The units of the progress bar.
     * @param autoPrint Whether or not to automatically print the progress bar after an update.
     */
    public ProgressBar(String title, long total, int width, String units, boolean autoPrint) {
        this.title = title;
        this.total = total;
        this.width = width;
        this.units = units;
        this.autoPrint = autoPrint;
    }
    
    /**
     * Creates a new ConsoleProgressBar object.
     *
     * @param title The title to display for the progress bar.
     * @param total The total size of the progress bar.
     * @param width The with of the bar in the progress bar.
     * @param units The units of the progress bar.
     */
    public ProgressBar(String title, long total, int width, String units) {
        this(title, total, width, units, DEFAULT_PROGRESS_BAR_AUTO_PRINT);
    }
    
    /**
     * Creates a new ConsoleProgressBar object.
     *
     * @param title The title to display for the progress bar.
     * @param total The total size of the progress bar.
     * @param units The units of the progress bar.
     */
    public ProgressBar(String title, long total, String units) {
        this(title, total, DEFAULT_PROGRESS_BAR_WIDTH, units);
    }
    
    /**
     * Creates a new ConsoleProgressBar object.
     *
     * @param title The title to display for the progress bar.
     * @param total The total size of the progress bar.
     * @param width The with of the bar in the progress bar.
     */
    public ProgressBar(String title, long total, int width) {
        this(title, total, width, "");
    }
    
    /**
     * Creates a new ConsoleProgressBar object.
     *
     * @param title The title to display for the progress bar.
     * @param total The total size of the progress bar.
     */
    public ProgressBar(String title, long total) {
        this(title, total, DEFAULT_PROGRESS_BAR_WIDTH, "");
    }
    
    
    //Methods
    
    /**
     * Builds the progress bar.<br>
     * This must be displayed with print(), not println().
     *
     * @return The progress bar.
     */
    @SuppressWarnings("HardcodedLineSeparator")
    public String get() {
        if (update) {
            progressBar = '\r' + spaces(width + ((String.valueOf(total).length() + units.length()) * 2) + 30) + '\r' +
                    getPercentageString() + ' ' +
                    getBarString() + ' ' +
                    getRatioString() + " - " +
                    getTimeRemainingString();
        }
        
        return progressBar;
    }
    
    /**
     * Updates the progress bar.<br>
     * If the time between updates is less than PROGRESS_BAR_MINIMUM_UPDATE_DELAY then the update will not take place until called again after the delay.
     *
     * @param newProgress The new progress of the progress bar.
     * @param autoPrint   Whather or not to automatically print the progress bar after an update.
     * @return Whether the progress bar was updated or not.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private synchronized boolean update(long newProgress, boolean autoPrint) {
        if (isComplete()) {
            return false;
        }
        
        if (firstUpdate == 0) {
            if (!title.isEmpty()) {
                System.out.println(Console.cyan(title + ": "));
                System.out.flush();
            }
            firstUpdate = System.nanoTime();
        }
        
        progress = truncateNum(newProgress, 0, total).longValue();
        
        if (((System.nanoTime() - currentUpdate) >= PROGRESS_BAR_MINIMUM_UPDATE_DELAY) || (progress == total)) {
            previous = current;
            current = progress;
            
            previousUpdate = currentUpdate;
            currentUpdate = System.nanoTime();
            
            update = true;
        }
        
        if (update && autoPrint) {
            print();
        }
        return update;
    }
    
    /**
     * Updates the progress bar.<br>
     * If the time between updates is less than PROGRESS_BAR_MINIMUM_UPDATE_DELAY then the update will not take place until called again after the delay.
     *
     * @param newProgress The new progress of the progress bar.
     * @return Whether the progress bar was updated or not.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public synchronized boolean update(long newProgress) {
        return update(newProgress, autoPrint);
    }
    
    /**
     * Adds one to the current progress.
     *
     * @return Whether the progress bar was updated or not.
     */
    public synchronized boolean addOne() {
        return update(progress + 1);
    }
    
    /**
     * Prints the progress bar to the console.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public synchronized void print() {
        String bar = get();
        System.out.print(bar);
        System.out.flush();
        System.err.flush();
        update = false;
    }
    
    /**
     * Calculates the ratio of the progress bar.
     *
     * @return The ratio of the progress bar.
     */
    public double getRatio() {
        return (double) current / total;
    }
    
    /**
     * Calculates the percentage of the progress bar.
     *
     * @return The percentage of the progress bar.
     */
    public int getPercentage() {
        return (int) (getRatio() * 100);
    }
    
    /**
     * Calculates the last recorded speed of the progress bar.
     *
     * @return The last recorded speed of the progress bar in units per second.
     */
    public double getLastSpeed() {
        double recentTime = (double) (currentUpdate - previousUpdate) / TimeUnit.SECONDS.toNanos(1);
        if ((recentTime == 0) || (previousUpdate == 0)) {
            return 0;
        }
        long recentProgress = current - previous;
        
        return recentProgress / recentTime;
    }
    
    /**
     * Calculates the average speed of the progress bar.
     *
     * @return The average speed of the progress bar in units per second.
     */
    public double getAverageSpeed() {
        double totalTime = (double) (currentUpdate - firstUpdate) / TimeUnit.SECONDS.toNanos(1);
        if ((totalTime == 0) || (firstUpdate == 0)) {
            return 0;
        }
        
        return current / totalTime;
    }
    
    /**
     * Estimates the time remaining in seconds.
     *
     * @return The estimated time remaining in seconds.
     */
    public long getTimeRemaining() {
        long remaining = total - current;
        if (remaining == 0) {
            return 0;
        }
        if (current == 0) {
            return Long.MAX_VALUE;
        }
        
        long timeRemaining = (long) (((double) remaining / (current - initialProgress)) * (currentUpdate - firstUpdate));
        return TimeUnit.NANOSECONDS.toSeconds(timeRemaining);
    }
    
    /**
     * Determines if the progress bar is complete or not.
     *
     * @return Whether the progress bar is complete or not.
     */
    public boolean isComplete() {
        return (current == total);
    }
    
    /**
     * Completes the progress bar.
     *
     * @param printTime      Whether or not to print the final time after the progress bar.
     * @param additionalInfo Additional info to print at the end of the progress bar.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void complete(boolean printTime, String additionalInfo) {
        update(total, false);
        String completeProgressBar = get();
        if (printTime) {
            long totalSeconds = initialDuration + ((currentUpdate - firstUpdate) / 1000000000);
            long totalMinutes = totalSeconds / 60;
            long totalHours = totalMinutes / 60;
            long totalDays = totalHours / 24;
            totalHours %= 24;
            totalMinutes %= 60;
            totalSeconds %= 60;
            String totalDuration = ((totalDays > 0) ? totalDays + "d " : "") +
                    ((totalDays > 0 || totalHours > 0) ? totalHours + "h " : "") +
                    ((totalDays > 0 || totalHours > 0 || totalMinutes > 0) ? totalMinutes + "m " : "") +
                    totalSeconds + "s";
            totalDuration = trim(totalDuration);
            completeProgressBar += " (" + totalDuration + ")";
        }
        if (!additionalInfo.isEmpty()) {
            completeProgressBar += " " + additionalInfo;
        }
        System.out.println(completeProgressBar);
        System.out.flush();
        System.err.flush();
        update = false;
    }
    
    /**
     * Completes the progress bar.
     *
     * @param printTime Whether or not to print the final time after the progress bar.
     */
    public void complete(boolean printTime) {
        complete(printTime, "");
    }
    
    /**
     * Completes the progress bar.
     */
    public void complete() {
        complete(true);
    }
    
    /**
     * Builds the percentage string for the progress bar.
     *
     * @return The percentage string.
     */
    public String getPercentageString() {
        int percentage = getPercentage();
        String percentageString = padLeft(String.valueOf(percentage), 3);
        
        return ((percentage == 100) ? Console.cyan(percentageString) : Console.green(percentageString)) + '%';
    }
    
    /**
     * Builds the progress bar string for the progress bar.
     *
     * @return The progress bar string.
     */
    public String getBarString() {
        double ratio = getRatio();
        int completed = (int) ((double) width * ratio);
        int remaining = width - completed;
        
        StringBuilder bar = new StringBuilder();
        bar.append('[');
        StringBuilder progress = new StringBuilder();
        progress.append("=".repeat(Math.max(0, completed)));
        if (completed != width) {
            progress.append('>');
            remaining--;
        }
        bar.append((completed == width) ? Console.cyan(progress.toString()) : Console.green(progress.toString()));
        bar.append(" ".repeat(Math.max(0, remaining)));
        bar.append(']');
        
        return bar.toString();
    }
    
    /**
     * Builds the ratio string for the progress bar.
     *
     * @return The ratio string.
     */
    public String getRatioString() {
        String formattedCurrent = padLeft(String.valueOf(current), String.valueOf(total).length());
        
        return ((current == total) ? Console.cyan(formattedCurrent) : Console.green(formattedCurrent)) +
                units + '/' +
                Console.cyan(String.valueOf(total)) +
                units;
    }
    
    /**
     * Builds the time remaining string for the progress bar.
     *
     * @return The time remaining string.
     */
    public String getTimeRemainingString() {
        long time = getTimeRemaining();
        
        if (current == total) {
            return Console.cyan("Complete");
        }
        if (time == Long.MAX_VALUE) {
            return "ETA: --:--:--";
        }
        
        int hours = (int) ((double) time / Duration.ofHours(1).getSeconds());
        time -= hours * TimeUnit.HOURS.toSeconds(1);
        
        int minutes = (int) ((double) time / Duration.ofMinutes(1).getSeconds());
        time -= minutes * TimeUnit.MINUTES.toSeconds(1);
        
        int seconds = (int) time;
        
        return "ETA: " + padZero(hours, 2) + ':' + padZero(minutes, 2) + ':' + padZero(seconds, 2);
    }
    
    
    //Setters
    
    /**
     * Sets the total progress of the progress bar.
     *
     * @param total The total progress of the progress bar.
     */
    public void setTotal(long total) {
        this.total = total;
    }
    
    /**
     * Sets the initial progress of the progress bar.
     *
     * @param initialProgress The initial progress of the progress bar.
     */
    public void setInitialProgress(long initialProgress) {
        this.initialProgress = initialProgress;
    }
    
    /**
     * Sets the initial duration of the progress bar in seconds.
     *
     * @param initialDuration The initial duration of the progress bar in seconds.
     */
    public void setInitialDuration(long initialDuration) {
        this.initialDuration = initialDuration;
    }
    
    /**
     * Sets the flag indicating whether or not to automatically print the progress bar after an update.
     *
     * @param autoPrint The flag indicating whether or not to automatically print the progress bar after an update.
     */
    public void setAutoPrint(boolean autoPrint) {
        this.autoPrint = autoPrint;
    }
    
    
    //String Functions
    
    /**
     * Trims the whitespace off of the front and back ends of a string.
     *
     * @param str The string to trim.
     * @return The trimmed string.
     */
    public static String trim(String str) {
        return lTrim(rTrim(str));
    }
    
    /**
     * Trims the whitespace off the left end of a string.
     *
     * @param str The string to trim.
     * @return The trimmed string.
     */
    public static String lTrim(String str) {
        return str.replaceAll("^[\\s\0]+", "");
    }
    
    /**
     * Trims the whitespace off the right end of a string.
     *
     * @param str The string to trim.
     * @return The trimmed string.
     */
    public static String rTrim(String str) {
        return str.replaceAll("[\\s\0]+$", "");
    }
    
    /**
     * Creates a string of the length specified filled with spaces.
     *
     * @param num The length to make the string.
     * @return A new string filled with spaces to the length specified.
     */
    public static String spaces(int num) {
        return fillStringOfLength(' ', num);
    }
    
    /**
     * Creates a string of the length specified filled with the character specified.
     *
     * @param fill The character to fill the string with.
     * @param size The length to make the string.
     * @return A new string filled with the specified character to the length specified.
     */
    public static String fillStringOfLength(char fill, int size) {
        return padRight("", size, fill);
    }
    
    /**
     * Pads a string on the right to a specified length.
     *
     * @param str     The string to pad.
     * @param size    The target size of the string.
     * @param padding The character to pad with.
     * @return The padded string.
     */
    public static String padRight(String str, int size, char padding) {
        if (str.length() >= size) {
            return str;
        }
        
        int numPad = size - str.length();
        char[] chars = new char[numPad];
        Arrays.fill(chars, padding);
        String pad = new String(chars);
        return str + pad;
    }
    
    /**
     * Pads a string on the left to a specified length.
     *
     * @param str     The string to pad.
     * @param size    The target size of the string.
     * @param padding The character to pad with.
     * @return The padded string.
     */
    public static String padLeft(String str, int size, char padding) {
        if (str.length() >= size) {
            return str;
        }
        
        int numPad = size - str.length();
        char[] chars = new char[numPad];
        Arrays.fill(chars, padding);
        String pad = new String(chars);
        return pad + str;
    }
    
    /**
     * Pads a string on the left to a specified length.
     *
     * @param str  The string to pad.
     * @param size The target size of the string.
     * @return The padded string.
     */
    public static String padLeft(String str, int size) {
        return padLeft(str, size, ' ');
    }
    
    /**
     * Pads a number string with leading zeros to fit a particular size.
     *
     * @param str  The number string to pad.
     * @param size The specified size of the final string.
     * @return The padded number string.
     */
    public static String padZero(String str, int size) {
        if (str.length() >= size) {
            return str;
        }
        
        return padLeft(str, size, '0');
    }
    
    /**
     * Pads a number string with leading zeros to fit a particular size.
     *
     * @param num  The number to pad.
     * @param size The specified size of the final string.
     * @return The padded number string.
     */
    public static String padZero(int num, int size) {
        return padZero(Integer.toString(num), size);
    }
    
    
    //Bound Functions
    
    /**
     * Forces a number within defined bounds.
     *
     * @param num The number value.
     * @param min The minimum value allowed.
     * @param max The maximum value allowed.
     * @return The truncated number.
     */
    public static Number truncateNum(Number num, Number min, Number max) {
        Number n = num;
        if (num.doubleValue() < min.doubleValue()) {
            n = min;
        }
        if (num.doubleValue() > max.doubleValue()) {
            n = max;
        }
        return n;
    }
    
}