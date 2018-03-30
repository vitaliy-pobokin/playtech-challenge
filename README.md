Playtech 2017 challenge:

Hi!

Thank you for choosing our challenge! Please follow the instructions below.

Implement Command line utility in java that:

- allows creating a user/password pair and storing in a text file;
- can authenticate vs. the stored user/password pairs in the file.

Add brief explanation in English about password chosen storage mechanism and security consideration about. The location of the file storing user/password pairs can be hardcoded in the user home directory, like "~/.passwords". Feel free to pick any open source libraries to help carry the task. The submited solutions will be judged by code quality via static analysis tools and the selected password mechanism schemes quality from security point of view.

The command line utility should be used like:

Add a new user:

 * <commandLine> add userName:password
 * the exit code 0 denotes success, anything other exit is a failure. Failures might be accompanied by an error in System.err stream.

To authenticate user:

 * <commandLine>auth userName:password
 * the exit code 0 denotes success, any other exit code is a failure.

Updates not supported.



Some thougths about password storage mechanism:

To store username:password pairs Linux-like way was chosen. Usernames saving unhashed to improve usability and search speed through the file. Hashed password attached to the username by “:” delimiter. To hash password was used bcrypt slow hashing function. This makes brute-force cracking less effective. Random salt generates for each new entry to prevent attacks with rainbow and lookup tables.