package ch.jntme.bfh.software;

public class Ticket {
    private String ticketName;
    private double ticketPrice;

    Ticket(String ticketName, double ticketPrice) {
        this.ticketName = ticketName;
        this.ticketPrice = ticketPrice;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getTicketName() {
        return ticketName;
    }
}
