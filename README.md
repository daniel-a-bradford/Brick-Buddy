# Brick-Buddy
A simple Lego part vendor search which finds the best deal based on the parts on your wish list.

This is my capstone project, built over the final three weeks of my Claim Academy Full Stack Java Bootcamp experience.

The frontend contains the files necessary for React to render an simple and fun set of pages. The backend is written in Java using Spring and configured to use MySQL. User passwords are stored in hashed form using PBKDF2 with HMAC SHA1. Currently, given the lack of an API for the purpose of searching across vendor sites, the part and vendor data are scraped from BrickLink.com.

Ensure you enter your MySQL connection username and password in src/main/resources/application-dev.properties.

I plan to allow users to add items to their want list by copying a URL from the Bricklink.com and pasting it in their want list input form. In addition, I intend to use Spring Security to secure the login progress and overcome the current cross origin security issue between backend and frontend servers.

Please take a look and enjoy! It may save you some money.

I welcome your feedback! Please email me at daniel.a.bradford@outlook.com with tips and suggestions.

The term 'Bricklink' is a trademark of Bricklink, Inc. Brick Buddy is not endorsed or certified by Bricklink, Inc.
