# Library Management System

A comprehensive client-server application for efficient library data management and processing.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Key Components](#key-components)
- [Classes](#classes)
  - [Client](#client)
  - [Server](#server)
  - [AdminWork](#adminwork)
  - [UserWork](#userwork)
  - [CheckLoginExists](#checkloginexists)
  - [CreateFile](#createfile)

## Overview

This application is designed for efficient data management and processing in a library setting. Its architecture is based on a client-server model that provides secure and reliable communication between client requests and the database.

## Architecture

The system is built on a client-server architecture, ensuring centralized data processing and secure communication.

## Key Components

1. **Server**: The main part of the application that provides centralized processing of requests from clients. It processes data, interacts with the database, and provides clients with the necessary information.

2. **Database**: A data repository used to permanently store and access information. The program works with a MySQL database.

3. **Client**: A text-based interface that allows users to interact with the application through an intuitive user interface. The client part provides functionality for sending queries to the server and displaying the resulting data.

## Classes

### Client

- Creates a JFrame named 'console'
- Features:
  - Change background color
  - Change foreground color
  - Increase text size
  - Save data from JTextArea to a file
  - Communicate with the server
- Methods:
  - `ReadFromServer`: Reads data from the socket and adds it to the TextArea
  - `WriteToServer`: Handles writing to the server
  - Listeners for user input (Enter key and 'Enter' button)
- Main method to initialize the Client and start communication threads

### Server

- Processes data and provides responses based on client input
- Features:
  - User login (verifies against 'usersec' table)
  - User signup (adds new user to 'usersec' table and creates user-specific library table)
  - Quit functionality

### AdminWork

Admin can perform 8 types of operations:
1. View all available books
2. Add a new book
3. Open a user's book
4. Add a book to a user's library
5. Delete a user
6. Add a user
7. Display information about users
8. Delete a book (from user's library or general library)
9. Return to main menu

### UserWork

Users can perform 4 types of operations:
1. View all available books
2. Add a new book
3. Open their own books
4. Add a book to their library
5. Return to main menu

### CheckLoginExists

An auxiliary class that checks if a login already exists in the 'usersec' table.

### CreateFile

An auxiliary class for saving data:
- Works with JFileChooser
- Allows users to choose a directory and save data from the text area
