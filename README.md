# Library Management Application
[![Development](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=IntelliJ-IDEA&logoColor=white)](https://www.jetbrains.com/idea/)
[![Language](https://img.shields.io/badge/Kotlin-7F52FF.svg?style=for-the-badge&logo=Kotlin&logoColor=white)](https://kotlinlang.org/)

This application was built using Intellij and programmed in the Kotlin.

## Description

For an assignment for Software Development Tools I developed a Library Management
app with Kotlin with CRUD functionality.


## Running Application

## Main Menu


         > ——————————————————————————————————
         > |      LIBRARY MANAGER APP       |
         > ——————————————————————————————————
         > |           BOOK MENU            |
         > |   1) Add a Book                |
         > |   2) Update a Book             |
         > |   3) Delete a Book             |
         > |   4) Archive a Book            |
         > |   5) Search title of a Book    |
         > |   6) More Options              |
         > |   20) Save books               |
         > |   21) Load books               |
         > ——————————————————————————————————
         > |   0) Exit                      |
         > ——————————————————————————————————
         >  Enter Option:

## More Options Menu


         > —————————————————————————————————
         > |       MORE OPTIONS MENU        |
         > —————————————————————————————————
         > |      LIST BOOK SUB-MENU        |
         > |   1) List all books            |
         > |   2) List active books         |
         > |   3) List archived books       |
         > |   4) List books by ID          |
         > —————————————————————————————————
         > |        EXTRA FEATURES          |
         > |   5) Add author to book        |
         > |   6) Search by author to book  |
         > |   7) Counts books by author    |
         > |   0) Exit                      |
         > —————————————————————————————————
         >     Enter Option:

## Authors

Contributor names and contact info

Mubarak Al
[@user-mk9](https://github.com/user-mk9)

## Version History

Version 4.0

Features in this app includes:

- Refactoring code for numberOf methods to use Lambdas
- Refactoring list functions to use Lambdas
- Add new function to search by title
- Adding Gradle tasks JUNIT and Coverage, KDoc Dokka and Linting
- Adding a new Model Class "Author"
- Adding and implementing task Version Checker

Version 3.0

Features in this app includes:

- Ability to Update, Delete, and Archive Books in the Collection
- Persistence of books to/from XML/JSON
- Listing of all, active or archived books.

Version 2.0

Features in this app includes:

- ArrayList Collection of Books - adding and listing functionality.
- JUnit tests for the new BookAPI class.

Version 1.0

Features in this app includes:

- Menu items for Adding, Listing, Updating and Deleting a Book. No Book model is added yet; the menu structure is a skeleton.
- Logging capabilities are added via MicroUtils Kotlin-Logging.
- a ScannerInput utility is included for rebust user I/O.