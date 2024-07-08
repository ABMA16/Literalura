# Challenge Literalura
This is a java challenge proposed to the back-end formation students in the Oracle Next Education (ONE) program.
This consist in the creation of a java program able to consume an API from [Gutendex]([https://www.exchangerate-api.com/](https://gutendex.com/)) which is a book database where we can create request in form of a simple URL.

## :checkered_flag: Objective
The objective of this project is to create a program where the user can choose from 7 types of request using the user generated database and the available information in gutendex.

## :hammer: Functionalities:
`Functionality 1`: Ask the user for a book title and in case it is already in the database, it informs the user, and if not, the program adds the book details and author (if not alredy there) into the database. \
`Functionality 2`: The program will show all books already stored in the database. \
`Functionality 3`: The program shows all the authors already registered in the database. \
`Functionality 4`: The program will ask the user for a year and the program will show a list of authors (registered in the database) that lived during that year. \
`Functionality 5`: The program will ask for a language and will show a list of books where the original language is the one given by the user. \
`Functionality 6`: The program will show the list of the top 10 most downloaded books already registered in the database. \
`Functionality 7`: The program will show some statistics from the books available in the database such as the amount of books in the database, the most and less downloaded book and the average downloads per book.

## :computer: Technologies
Java 17 \
IntellJ V21 \
API (Application Programming Interface) \
Insomnia API platform
Postgres database

## :bulb: How to use?
Once the program starts it shows the main manu to the user where he can choose 1 from 7 functionalities using a number from 0 to 7, where 0 is the command to stop the program. If the user gives other number, the program will simply print the message "opcion invalida" and will show the menu again.
According to the selected functionality, the user will have to write additionally the name of a book, the name of an author or a date to fulfill the request.
Only in the first functionality will create a request for the API, while the other will create a SQL query for the local database.