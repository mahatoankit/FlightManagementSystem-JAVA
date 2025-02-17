package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.SwingUtilities;

public class LoadGUI implements Command {
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        SwingUtilities.invokeLater(() -> new LoginWindow(flightBookingSystem));
    }
}
