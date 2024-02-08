package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Seat {
    private int row;
    private int column;
    private int price;
    @JsonIgnore
    private boolean booked = false;
    @JsonIgnore
    private UUID token = UUID.randomUUID();

    Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        this.price = price;
    }

    Seat() {

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    public int getPrice() {
        return price;
    }
    public boolean isBooked() {
        return booked;
    }
    public void setBooked(boolean booked) {
        this.booked = booked;
    }
    public UUID getToken() {
        return token;
    }
    public void setToken(UUID token) {
        this.token = token;
    }
}
