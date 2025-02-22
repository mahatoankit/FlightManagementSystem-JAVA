package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.commands.Command;

import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        FlightBookingSystem fbs = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            fbs = FlightBookingSystemData.load();

            System.out.println("Flight Booking System");
            System.out.println("Enter 'help' for command list or 'loadgui' to launch GUI");

            while (true) {
                System.out.print("> ");
                String line = br.readLine();

                if (line.trim().equalsIgnoreCase("exit")) {
                    if (fbs != null) {
                        FlightBookingSystemData.store(fbs);
                    }
                    System.exit(0);
                }

                try {
                    Command command = CommandParser.parse(line);
                    command.execute(fbs);
                } catch (FlightBookingSystemException ex) {
                    System.err.println("Command error: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        } catch (FlightBookingSystemException ex) {
            System.err.println("System Error: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
}