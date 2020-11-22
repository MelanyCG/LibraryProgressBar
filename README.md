# LibraryProgressBar

[![](https://jitpack.io/v/MelanyCG/LibraryProgressBar.svg)](https://jitpack.io/#MelanyCG/LibraryProgressBar)

Library for creating a progress bar that shows how many steps were made out of the total.
Have fun!

## Setup
Step 1. Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency:
```gradle
dependencies {
     implementation 'com.github.MelanyCG:LibraryProgressBar:1.00.01'
}
```
## Usage
![](ProgressBarApp.gif)

## How to use - Basic progress bar:
1. Add this code to your xml layout activity: 
```java
    <com.example.progressbarlibrary.MyProgressBar
        android:id="@+id/myProgressBar"
        android:layout_width="218dp"
        android:layout_height="251dp"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="70dp"
        app:layout_constraintDimensionRatio="1:1"
        app:bottom_text="Example"
        app:bottom_text_size="25dp"/>
```

2. Add this code to your main:
```java
        myProgressBar = findViewById(R.id.myProgressBar);
        myProgressBar.setText(3,4);
```

# Usage
![](example1.jpeg)

## How to use - A progress bar that advanced until the end:
1. Add this code to your xml layout activity: 
```java
    <com.example.progressbarlibrary.MyProgressBar
        android:id="@+id/myProgressBar"
        android:layout_width="218dp"
        android:layout_height="251dp"
        android:layout_below="@+id/textView"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="70dp"
        app:layout_constraintDimensionRatio="1:1" />
```
2. Add this code to your main:
        myProgressBar = findViewById(R.id.myProgressBar);

        // Random the colors of the arc progress bar and the text of the progress bar.
        Random rnd = new Random();
        int finishColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int unFinishColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int textColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        myProgressBar.setText(1,10);
        myProgressBar.setStrokeWidth(30);
        myProgressBar.setTextSize(100);
        myProgressBar.setTextColor(textColor);
        myProgressBar.setUnfinishedStrokeColor(unFinishColor);
        myProgressBar.setFinishedStrokeColor(finishColor);
        myProgressBar.setBottomText("Example");
        myProgressBar.setBottomTextSize(50);

        mTimedHandler.sendEmptyMessageDelayed(0,5);
```
# Usage
![](example2.jpeg)
