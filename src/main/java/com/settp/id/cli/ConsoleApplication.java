package com.settp.id.cli;

import com.settp.id.core.exception.*;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.service.CentralAuthority;
import com.settp.id.core.service.OtherAuthority;

import java.util.Map;
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

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                running = handleChoice(choice);
            } catch (IdentityNotFoundException | IllegalStatusChangeException | ImmutableChangeException e) {
                System.out.println("\n[REJECTED] " + e.getMessage());
            }  catch (DataAccessException e) {
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

    private void printMenu() {
        System.out.printf("\n %s Menu%n", userRole.name());

        if (userRole == Organisation.CENTRAL_AUTHORITY) {
            System.out.println("1. Create new ID");
            System.out.println("2. Update status of an ID");
            System.out.println("3. Update ID data");
            System.out.println("4. View ID data");
            System.out.println("5. Exit");
        } else {
            System.out.println("1. View all ID data");
            System.out.println("2. Check if status is valid");
            System.out.println("3. Exit");
        }
            System.out.println("Select an option: ");
    }

    private boolean handleChoice(String choice) {
        if (userRole == Organisation.CENTRAL_AUTHORITY) {
            return handleCentralAuthorityChoice(choice);
        } else {return handleOtherAuthorityChoice(choice);
        }
    }

    private boolean handleCentralAuthorityChoice(String choice) {
        switch (choice) {
            case "1":
                String newUuid = managementService.createIdentity(userRole);
                System.out.println("[Success] Created new identity with UUID: " + newUuid);
                return true;
            case "2":
                System.out.println("Enter UUID: ");
                String uuidToUpdate = scanner.nextLine();
                System.out.println("Enter new Status (ACTIVE, SUSPENDED, REVOKED): ");
                String statusStr = scanner.nextLine().toUpperCase();

                managementService.statusUpdate(uuidToUpdate, IdentityStatus.valueOf(statusStr), userRole);
                System.out.println("[SUCCESS] Status updated");
                return true;
            case "3":
                System.out.println("Enter UUID: ");
                String attrUuid = scanner.nextLine();
                System.out.println("Enter attribute to update: ");
                String attrName = scanner.nextLine();
                System.out.println("Enter updated value: ");
                String attrValue = scanner.nextLine();

                managementService.updateAttribute(attrUuid, attrName, attrValue, userRole);
                System.out.println("[SUCCESS] Attribute updated");
                return true;
            case "4":
                viewIdentity();
                return true;
            case "5":
                return false;
            default:
                System.out.println("[ERROR] Invalid option, enter a number from 1-5");
                return true;
        }
    }

    private boolean handleOtherAuthorityChoice(String choice) {
        switch (choice) {
            case "1":
                viewIdentity();
                return true;
            case "2":
                doQuickCheck();
                return true;
            case "3":
                return false;
            default:
                System.out.println("[ERROR] Invalid option, enter a number from 1-3");
                return true;
        }
    }

    private void viewIdentity() {
        System.out.println("Enter UUID to view: ");
        String uuid = scanner.nextLine();

        Map<String, String> uuidData = verificationService.verifyIdentity(uuid, userRole);

        System.out.println("\n--- ID Record ---");
        uuidData.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        System.out.println("-----------------------");
    }

    private void doQuickCheck() {
        System.out.println("Enter UUID to check status of: ");
        String uuid = scanner.nextLine();

        boolean isValid = verificationService.statusValidityCheck(uuid);
        System.out.println("\n--- Status Check ---");
        System.out.println("Is the ID ACTIVE? " + (isValid ? "YES" : "NO"));
        System.out.println("-----------------------");
    }
}
