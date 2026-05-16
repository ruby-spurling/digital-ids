# Digital ID's - SETTP Assesment 2

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
├── pom.xml                      <-- Dependency configuration for Maven/Gradle & JUnit
└── README.md                    <-- Project documentation
```
### Command Query Responsibility Segregation
### Data Minimisation and Attribute Based Access Control
### Encapsulation

## Business Rules
## Testing and CI
## Agile Practice