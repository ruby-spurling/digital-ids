# Digital ID's - SETTP Assessment 2

Repository link: https://github.com/ruby-spurling/digital-ids

## How to run

## System Structure and Main Components
### File Structure
```text
digital-ids/
├── .github/
│   └── workflows/
│       └── maven-ci.yml               <-- Automated CI/CD pipeline configuration
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── settp/
│   │               └── id/
│   │                   ├── Main.java               <-- System Entry Point
│   │                   ├── cli/
│   │                   │   └── ConsoleApplication.java <-- Console Menu User Interface
│   │                   └── core/
│   │                       ├── exception/
│   │                       │   ├── AttributeDoesNotExistException.java
│   │                       │   ├── DataAccessException.java
│   │                       │   ├── DataLoadException.java
│   │                       │   ├── DataSaveException.java
│   │                       │   ├── IdentityNotFoundException.java
│   │                       │   ├── IllegalStatusChangeException.java
│   │                       │   ├── ImmutableChangeException.java
│   │                       │   └── UnauthorisedAccessException.java
│   │                       ├── model/
│   │                       │   ├── DigitalID.java         <-- Base Identity Data Model
│   │                       │   ├── IdentityStatus.java    <-- Enum (ACTIVE, SUSPENDED, REVOKED)
│   │                       │   └── Organisation.java      <-- Enum (BANK, EMPLOYER, etc.)
│   │                       ├── repository/
│   │                       │   ├── IdentityRepository.java       <-- Repository Interface
│   │                       │   ├── JsonIdentityRepository.java   <-- JSON Implementation
│   │                       │   └── MemoryIdentityRepository.java <-- Memory Implementation for unit testing
│   │                       └── service/
│   │                           ├── CentralAuthority.java  <-- Identity Management (Management)
│   │                           ├── OtherAuthority.java    <-- Identity Consumption (Verification)
│   │                           └── SecurityLogger.java    <-- Logs activity for audit purposes
│   └── test/
│       └── java/
│           └── com/
│               └── settp/
│                   └── id/
│                       └── core/
│                           ├── model/
│                           │   └── DigitalIDTest.java
│                           ├── repository/
│                           │   └── JsonIdentityRepositoryTest.java
│                           └── service/
│                               ├── CentralAuthorityTest.java
│                               └── OtherAuthorityTest.java
├── identities.json              <-- JSON storage database
├── audit.log                    <-- The persistent generated security audit trail
├── pom.xml                      <-- Dependency configuration for JUnit
└── README.md                    <-- Project documentation
```
### Layered Architecture
The system is structured as a layered architecture where the components are split into cli, exceptions, model, repository and service.
### Command Query Responsibility Segregation
The central and other authorities are split in order to adhere to the single responsibility principle. The management of identities (through the central authority) and their consumption (through the other authorities) are treated as distinct capabilities, and as such are housed in separate service classes.
### Data Minimisation and Attribute Based Access Control
Wherever possible, data has been minimised. All authorities are only able to view their relevant attributes and fields, such as date of birth, are transformed to provide the authority with enough information (over_18), without giving out too much of a users personal data.
### Dependency Inversion Principle
Business processes do not rely on file storage logic. Using the IdentityRepository interface allows the system to seamlessly switch between using the memory and JSON repositories for testing and production.
### Open Closed Principle
Adding a new organisation to this system would be simple. It is not bound by having a specific number of other authorities or allowed attributes. All that has to be done is adding the name to the Organisation enum, and adding the attributes in the OtherAuthorityClass and UI.

## Business Rules
## ID Rules
 - Digital ID's must contain a minimum of a name, date of birth and an automatically generated status and UUID
 - When an ID is revoked, the attributes and status can no longer be modified
 - Name, date of birth and NI number are immutable once set
 - Unauthorised actions are consistently rejected using custom exceptions
 - All actions must be logged by the security logger
## Authority Rules
 - **Central Authority**: Can create, update status, update attributes, and view ID's
 - **Employer**: Can view the right to work, check if over 18, and verify the validity of an ID
 - **Tax Service**: Can view the residency status, NI number, and verify the validity of an ID
 - **Driving License Authority**: Can view driving restrictions, license category, penalty points, eligibility, and verify the validity of an ID
 - **Bank**: Can only verify the validity of an ID