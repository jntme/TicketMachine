package ch.jntme.bfh.software;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    // member variables
    Ticket ticket = null;
    double resPrice = 0.0;

    TextArea console;
    TextField remPriceTextField;
    TextField enteringMoneyTextField;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        // main layout
        final VerticalLayout layout = new VerticalLayout();

        // ticket buttons -----------------------------------------------
        Ticket t1 = new Ticket("T1", 11.00);
        Ticket t2 = new Ticket("T2", 15.00);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(t1);
        tickets.add(t2);

        final HorizontalLayout ticketLayout = new HorizontalLayout();
        tickets.forEach(t -> {
            Button btn = new Button(t.getTicketName());
            btn.addClickListener(event -> ticketSelected(t));
            ticketLayout.addComponent(btn);
        });

        layout.addComponent(ticketLayout);

        // --------------------------------------------------------------

        // rem. price label ---------------------------------------------

        final HorizontalLayout remPriceLayout = new HorizontalLayout();

        Label remPriceLabel = new Label("Rem. Price:");
        this.remPriceTextField = new TextField();
        remPriceTextField.setValue(String.valueOf(this.resPrice));
        remPriceTextField.setEnabled(false);

        Button cancelButton = new Button("cancel");
        cancelButton.addClickListener(event -> cancel());

        remPriceLayout.addComponents(remPriceLabel, remPriceTextField, cancelButton);

        layout.addComponent(remPriceLayout);

        // --------------------------------------------------------------

        // input money part ---------------------------------------------

        final HorizontalLayout moneyPartLayout = new HorizontalLayout();

        enteringMoneyTextField = new TextField("Input Money:");
        enteringMoneyTextField.setEnabled(false);
        Button putInButton = new Button("put in");

        putInButton.addClickListener(event -> {
            if(enteringMoneyTextField.getValue() != "")
            putInMoney(Double.parseDouble(enteringMoneyTextField.getValue()));
        });

        moneyPartLayout.addComponents(enteringMoneyTextField, putInButton);
        layout.addComponent(moneyPartLayout);

        // --------------------------------------------------------------

        // console ------------------------------------------------------

        console = new TextArea();
        console.setWidth("700px");
        layout.addComponent(console);

        // --------------------------------------------------------------
        setContent(layout);
    }

    private void putInMoney(double v) {
        logToConsole("Got money put in: " + Double.toString(v));

        this.resPrice -= v;

        checkBalance();
    }

    private void checkBalance() {
        if(resPrice <= 0.0) {
            logToConsole(ticket.getTicketName() + " bought.");

            if(resPrice < 0.0) logToConsole("Returning $" + resPrice * -1 + ".");

            logToConsole(ticket.getTicketName() + " printed.");

            reset();
        }
        else {
            logToConsole(resPrice + " remaining.");
            this.remPriceTextField.setValue(String.valueOf(resPrice));
        }
    }

    private void ticketSelected(Ticket t) {
        logToConsole(t.getTicketName() + " got selected.");
        ticket = t;
        resPrice = t.getTicketPrice();
        this.remPriceTextField.setValue(String.valueOf(resPrice));
        this.enteringMoneyTextField.setEnabled(true);
    }

    private void reset() {
        this.ticket = null;
        this.resPrice = 0.0;
        this.remPriceTextField.setValue(String.valueOf(resPrice));
        this.enteringMoneyTextField.setEnabled(false);
        this.enteringMoneyTextField.setValue("");
    }

    private void cancel() {
        reset();

        logToConsole("Cancelling. Everything gets resetted.");
    }

    private void logToConsole(String logMessage) {
        this.console.setValue(logMessage + "\n" + this.console.getValue());
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}