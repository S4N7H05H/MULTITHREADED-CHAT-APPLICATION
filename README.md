# MULTITHREADED-CHAT-APPLICATION

*COMPANY* : CODETECH IT SOLUTIONS

*NAME* : A SANTHOSH KUMAR

*INTERN ID* : CT04DR510

*DOMAIN* : JAVA PROGRAMMING

*DURATION* : 4 WEEKS

*MENTOR* : NEELA SANTHOSH

# Description

This task involves developing a real-time client–server chat application using Java Sockets and Multithreading. The goal is to enable multiple clients to communicate simultaneously through a centralized server, demonstrating concepts of network programming, concurrency, and inter-process communication.

The project consists of two main components:

Server Program: Listens for incoming client connections on a specified port (ServerSocket) and assigns each client to a dedicated handler thread using an ExecutorService. The server maintains a shared list of active clients and broadcasts messages to all connected users.

Client Program: Connects to the server via Socket, sends messages, and runs a separate receiver thread to display incoming messages in real time.

Each client can send and receive messages concurrently, and can gracefully exit using the /quit command. The application includes proper error handling for connection issues and disconnections, ensuring a stable multi-user chat environment.

This project effectively demonstrates how Java handles multithreaded networking, resource synchronization, and concurrent client management — forming the foundation for building scalable chat servers and real-time communication systems.

# OUTPUT 

<img width="1920" height="1080" alt="Image" src="https://github.com/user-attachments/assets/66cf8111-5ac1-40d2-a1fe-b12cfc6e31fd" />
