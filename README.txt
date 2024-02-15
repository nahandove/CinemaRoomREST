Project Assignment from JetBrains Academy (www.hyperskill.org), Java Backend Developer track

Summary: A RESTful application for a movie theater. The client can book and return tickets, 
or look up current statistics on ticket booking and income.

"Cinema Room REST Service" is a project based on the Spring Boot framework. The application
manages a small cinema room with 9 rows and 9 columns. The seats at the front 4 rows cost
10 dollars, while the rest of the seats cost 8 dollars. The client can obtain seat info or
statistics via GET request, and purchase or return ticket via POST requests.

The program assigns the local server port to 28852, and all client requests are served by the 
server host address: http://www.localhost:28852 together with the following links:

/seats
/purchase
/return
/stats

--Request links in detail

1. /seats: When the "/seats" link is used, the application returns the complete theater with
information on all current available seats in the following JSON format. Sold seats are not
displayed in the response. Example response with seat at row 9 and column 8 purchased:

{
   "rows": 9,
   "columns": 9,
   "seats": [
      {
         "row": 1,
         "column": 1,
	 "price": 10
      },

      ........

      {
         "row": 9,
         "column": 7,
	 "price": 8
      },
      {
         "row": 9,
         "column": 9
	 "price": 8
      }
   ]
}

2. /purchase: The "/purchase" link performs POST request for clients who want to book specific
tickets. The client must supply the request as JSON in the format: {"row":x,"column":y}, where
x and y are numbers from 1 to 9. If "row" or "column" are null or out of bounds (less than 0
or greater than 9), the server returns the error response: {"error":"The number of a row or a 
column is out of bounds!"}. If the requested seat is already booked, the server returns the
error message: {"error":"The ticket has already been purchased"}. For both errors, the server
also returns status core 400 (Bad Request).

Once the server receives valid client POST requests, the server marks the seat as booked and
sends the client information about a specific ticket (row, column, and price). The client also
receives a token associated with the booked seat, which must be used if the client wants to
return a booked ticket. Let's say the client books the seat at row 3 and column 4. The server
returns the ticket information along with an unique token:

{
"token": "00ae15f2-1ab6-4a02-a01f-07810b42c0ee",
"ticket": {
       "row": 3
       "column": 4
       "price": 10
    }
}

Once a ticket has been booked, the specific seat will not be shown in the "/seats" links and
unavailable for subsequent purchases.

3. /return: The "/return" link performs POST request for clients wishing to return a ticket.
The client must supply the token obtained from their previous ticket purchase in the following
format. Say the client who bought the ticket in section 2 now cannot go to the theater for
the evening. The client can make a return request by posting the token:

{
"token": "00ae15f2-1ab6-4a02-a01f-07810b42c0ee",
}

The server checks if the token matches that of any of the previously purchased tickets. Once a
match is found, the server returns the client's ticket information:

{
"ticket": {
       "row": 3
       "column": 4
       "price": 10
    }
}

The seat will be once again be marked as available for purchase and show up in the "/seats"
links. Note that once the purchased seat is returned, a new token associated with the seat
is generated, and the old token expires.

If the token fails to match any of the seat tickets, the server sends the error message: {
"error":"Wrong token!"} with the status code 400 (Bad Request).

4. /stats

The client can inquire the purchased seats, available seats, and income statistics by using 
the "/stats" link with the "password" query parameter. THe password is currently set as
"super_secret". When the parameter is correct, the server returns the current status of
the cinema, for example, if three front-row tickets from the cinema were purchased:

{
"income": 30
"available": 78
"purchased": 3
} 

If the client supplies the wrong password or if the query does not contain the "password" query
parameter, the server returns the error message: {"error": "The password is wrong!"} with the
error code 401 (Unauthorized).

E. Hsu (nahandove@gmail.com)
February 8th, 2024
