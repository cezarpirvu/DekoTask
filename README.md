# The DekoTask

* Read the users from the xml, csv and json files within the `data` directory
* Merge all users into a single list and sort them by their `userId` in ascending order
* Write the ordered results to new xml, csv and json files, see the `examples` directory
  * Results should use the same structure as the source files they were parsed from
  * The exception is for `lastLoginTime` where an `ISO 8601` date format is preferred for output
