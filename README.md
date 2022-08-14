# Car Wash Controller
A car wash controller simulator with a GUI. 
The GUI is created by the UI designer of IntelliJ Idea. It works as intended. After running the program, the wash steps can be chosen, and when the "Start" button is pressed, the program checks if the car is in the right station.
It can quickly be used by clicking the executable .jar file.

- This application is developed using Java since it is my main focus right now. But I am pretty sure this is not a good Java code because I used the methods of the main class like functions. Not really in line with OOP. 

## Start Operation
If it is;
- starts the timer while continuously checking the slider position.
If not;
- "Vehicle out of Position" button is enabled, and the program waits for the slider to slide into the right place.

## Stop Operation
Stop operation works by checking a stop flag of the main class. If it is set, the program exits and returns to its initial state.

## Slider Checking
The slider is continuously checked, and if there is a discrepancy between its position and the required station for the operation, the operation stops. The timer is frozen. When the slider returns to its correct place, it starts working, and the timer begins where it is left off.