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
        try {
            FlightBookingSystem fbs = FlightBookingSystemData.load();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("Flight Booking System");
            System.out.println("Enter 'help' for command list or 'loadgui' to launch GUI");
            
            while (true) {
                System.out.print("> ");
                String line = br.readLine();
                
                if (line.trim().equalsIgnoreCase("exit")) {
                    break;
                }
                
                try {
                    Command command = CommandParser.parse(line);
                    command.execute(fbs);
                } catch (FlightBookingSystemException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            
            FlightBookingSystemData.store(fbs);
            
        } catch (IOException | FlightBookingSystemException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}