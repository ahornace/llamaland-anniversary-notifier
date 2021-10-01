# Llamaland Anniversary Notifier

Simple program for notifying of upcoming anniversaries.

### Requirements "change"
Requirements cited:
> When a citizen of Llamaland turns 100 years old the King would like to send them a personal email. He would like to 
> be notified who should be sent an email at least five weekdays in advance (the King never works weekends). If there 
> are "a lot" of people turning 100 years old on a particular day (a term we now understand to mean more than 20 people)
> the King would like 10 weekdays notice.

The important part was **at least** and the program adheres only to the 10 weekday notice which still should satisfy
the requirement. This makes the program a bit simpler.

### Performance
Only simple approach was used and some optimizations would be needed to properly handle the load of `10^9` citizens.

### Testing
The use of `mockito` would be nice for statically mocking `LocalDate.now()` for proper integration tests.

### Improvements Needed
- Proper program argument handling with ability to override anniversary and notice times.
- Better regexes for parsing the citizen entries.
