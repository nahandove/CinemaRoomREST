package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class Controller {
    static List<Seat> seats = new CopyOnWriteArrayList<>();

    static {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                seats.add(new Seat(i + 1, j + 1, 10));
            }
        }
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                seats.add(new Seat(i + 1, j + 1, 8));
            }
        }
    }

    @GetMapping("/seats")
    public CinemaRoom showCinemaRoom() {
        return new CinemaRoom(9, 9, getAvailableSeats());
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new CopyOnWriteArrayList<>();
        for (Seat seat: seats) {
            if (!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> bookTicket(@RequestBody Seat seatRequest) {
        for (Seat seat : seats) {
            if (seat.getRow() == seatRequest.getRow() && seat.getColumn() == seatRequest.getColumn()) {
                if (seat.isBooked()) {
                    return ResponseEntity
                            .badRequest()
                            .body("{\"error\": \"The ticket has been already purchased!\"}");
                    }
                seat.setBooked(true);
                return ResponseEntity
                        .ok()
                        .body("{\"token\": "
                                + "\"" + seat.getToken() + "\""
                                +",\"ticket\": {"
                                + "\"row\": " + seat.getRow() + ","
                                + "\"column\": " + seat.getColumn() + ","
                                + "\"price\": " + seat.getPrice()
                                +"}}");
            }
        }
        return ResponseEntity
                .badRequest()
                .body("{\"error\": \"The number of a row or a column is out of bounds!\"}");

    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody String returnToken) {
        try {
            String tokenString = returnToken.split(":\\s*")[1];
            UUID token = UUID.fromString(tokenString.substring(1, tokenString.length() - 2));
            for (Seat seat : seats) {
                if (token.equals(seat.getToken())) {
                    seat.setBooked(false);
                    seat.setToken(UUID.randomUUID());
                    return ResponseEntity
                            .ok()
                            .body("{\"ticket\": {"
                                    + "\"row\": " + seat.getRow() + ","
                                    + "\"column\": " + seat.getColumn() + ","
                                    + "\"price\": " + seat.getPrice()
                                    + "}}");
                }
            }
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Wrong token!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Wrong token!\"}");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(defaultValue="no_password") String password) {
        if ("super_secret".equals(password)) {
            int income = 0;
            int available = getAvailableSeats().size();
            int purchased = 81 - available;
            for (Seat seat : seats) {
                if (seat.isBooked()) {
                    income += seat.getPrice();
                }
            }
            return ResponseEntity
                    .ok()
                    .body("{\"income\":" + income + ","
                            + "\"available\":" + available + ","
                            + "\"purchased\":" + purchased
                            + "}");
            }
        return new ResponseEntity<String>("{\"error\":\"The password is wrong!\"}", HttpStatus.UNAUTHORIZED);
    }
}
