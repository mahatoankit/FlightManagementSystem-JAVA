package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Payment;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDataManager implements DataManager {
    private static final String FILENAME = "resources/payments.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split("::");
                int bookingId = Integer.parseInt(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                String cardNumber = parts[2];
                String expiryDate = parts[3];
                LocalDate paymentDate = LocalDate.parse(parts[4]);

                Payment payment = new Payment(bookingId, amount, cardNumber, expiryDate, paymentDate);
                fbs.addPayment(payment);
                line = br.readLine();
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILENAME))) {
            for (Payment payment : fbs.getPayments()) {
                out.println(String.format("%d::%f::%s::%s::%s",
                        payment.getBookingId(),
                        payment.getAmount(),
                        payment.getCardNumber(),
                        payment.getExpiryDate(),
                        payment.getPaymentDate()));
            }
        }
    }
}