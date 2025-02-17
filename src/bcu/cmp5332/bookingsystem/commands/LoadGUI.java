package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.SwingUtilities;

/**
 * Command class that launches the graphical user interface for the flight booking system.
 * This class implements the Command interface and provides functionality to switch
 * from the console-based interface to a GUI by opening the login window.
 */
public class LoadGUI implements Command {
    
    /**
     * Executes the load GUI command by creating and displaying the login window
     * on the Event Dispatch Thread using SwingUtilities.
     *
     * @param flightBookingSystem the flight booking system to be displayed in the GUI
     * @throws FlightBookingSystemException if there is an error while initializing the GUI
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        SwingUtilities.invokeLater(() -> new LoginWindow(flightBookingSystem));
    }
}