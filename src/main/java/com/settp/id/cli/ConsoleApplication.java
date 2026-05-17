package com.settp.id.cli;

import com.settp.id.core.exception.*;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.service.CentralAuthority;
import com.settp.id.core.service.OtherAuthority;

import java.util.Scanner;

public class ConsoleApplication {
    private final CentralAuthority managementService;
    private final OtherAuthority verificationService;
    private final Scanner scanner;
    private Organisation userRole;

    public ConsoleApplication(CentralAuthority managementService, OtherAuthority verificationService) {
        this.managementService = managementService;
        this.verificationService = verificationService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=========================================");
        System.out.println("   Digital Identity Management System    ");
        System.out.println("=========================================");

        authenticateUser();

        CentralAuthorityUI centralUI = new CentralAuthorityUI(managementService, verificationService, scanner, userRole);
        OtherAuthorityUI otherUI = new OtherAuthorityUI(verificationService, scanner, userRole);

        boolean running = true;
        while (running) {
            try {
                if (userRole == Organisation.CENTRAL_AUTHORITY) {
                    centralUI.printMenu();
                    String choice = scanner.nextLine();
                    running = centralUI.handleChoice(choice);
                } else {
                    otherUI.printMenu();
                    String choice = scanner.nextLine();
                    running = otherUI.handleChoice(choice);
                }

            } catch (IdentityNotFoundException | IllegalStatusChangeException | ImmutableChangeException e) {
                System.out.println("\n[REJECTED] " + e.getMessage());
            } catch (DataAccessException e) {
                System.out.println("\n[ERROR] Database failure: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("\n[INVALID INPUT] Please provide a valid input");
            }
        }
        System.out.println("Shutting down system...");
        scanner.close();
    }

    private void authenticateUser() {
        System.out.println("Select user to log in as:");
        System.out.println("1. Central Authority (Management)");
        System.out.println("2. Employer (Verification)");
        System.out.println("3. Tax Service (Verification)");
        System.out.println("4. Driving License Authority (Verification)");
        System.out.println("5. Bank (Verification)");

        while (userRole == null) {
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userRole = Organisation.CENTRAL_AUTHORITY;
                    break;
                case "2":
                    userRole = Organisation.EMPLOYER;
                    break;
                case "3":
                    userRole = Organisation.TAX_SERVICE;
                    break;
                case "4":
                    userRole = Organisation.DRIVING_LICENSE_AUTHORITY;
                    break;
                case "5":
                    userRole = Organisation.BANK;
                    break;
                default:
                    System.out.println("[ERROR] Invalid user choice, enter a number from 1-5");
            }
        }
        System.out.println("[SUCCESS] Logged in as " + userRole.name());
    }

}
